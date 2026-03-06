/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.service;

import com.Lesly.ConvertirFichier.repository.DocumentsRepository;
import org.springframework.transaction.annotation.Transactional;
import com.Lesly.ConvertirFichier.model.Utilisateur;
import com.Lesly.ConvertirFichier.model.Document;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.convertapi.client.*;
import java.time.LocalDateTime;

/**
 *
 * @author jeanl
 */

@Service
@Transactional
public class ServiceDocument {
    
    private final DocumentsRepository documentRepository;
    
    @Value("${convertapi.secret}")
    private String convertApiSecret;
    
    @Value("${upload.directory}")
    private String uploadDir;
    
    private Config convertApiConfig;
    
    // Injection de dépendance via constructeur
    public ServiceDocument(DocumentsRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
    
    // Initialisation de ConvertAPI
    @jakarta.annotation.PostConstruct
    public void init() {
        this.convertApiConfig = Config.defaults(convertApiSecret);
    }
    
    public File convertPdfToWord(File fichierPdf) throws Exception {
        CompletableFuture<ConversionResult> future =
            ConvertApi.convert(
                "pdf",
                "docx",
                new Param[] { new Param("file", fichierPdf.toPath()) },
                convertApiConfig
            );
        
        ConversionResult result = future.get();
        result.saveFilesSync(fichierPdf.getParentFile().toPath());
        
        return new File(
            fichierPdf.getParent(),
            fichierPdf.getName().replace(".pdf", ".docx")
        );
    }
    
    public File convertPdfToExcel(File fichierPdf) throws Exception {
        CompletableFuture<ConversionResult> future =
            ConvertApi.convert(
                "pdf",
                "xlsx",
                new Param[] { new Param("file", fichierPdf.toPath()) },
                convertApiConfig
            );
        
        ConversionResult result = future.get();
        result.saveFilesSync(fichierPdf.getParentFile().toPath());
        
        return new File(
            fichierPdf.getParent(),
            fichierPdf.getName().replace(".pdf", ".xlsx")
        );
    }
    
    public File convertWordToPdf(File fichierWord) throws Exception {
        CompletableFuture<ConversionResult> future =
            ConvertApi.convert(
                "docx",
                "pdf",
                new Param[] { new Param("file", fichierWord.toPath()) },
                convertApiConfig
            );
        
        ConversionResult result = future.get();
        result.saveFilesSync(fichierWord.getParentFile().toPath());
        
        return new File(
            fichierWord.getParent(),
            fichierWord.getName().replace(".docx", ".pdf")
        );
    }
    
    public Document convertir(
            HttpSession session, 
            MultipartFile fichier,
            String typeApres) throws Exception {
        
        Utilisateur u = (Utilisateur) session.getAttribute("utilisateurConnecte");
        
        if (fichier == null || fichier.isEmpty()) {
            throw new Exception("Aucun fichier sélectionné.");
        }
        
        String nomOriginal = fichier.getOriginalFilename();
        String typeAvant = getExtension(nomOriginal);
        
        if (!isConversionAutorisee(typeAvant, typeApres)) {
            throw new Exception("Conversion non autorisée.");
        }
        
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String nomStockage = System.currentTimeMillis() + "_" + nomOriginal;
        Path fichierStockePath = uploadPath.resolve(nomStockage);
        File fichierStocke = fichierStockePath.toFile();
        
        fichier.transferTo(fichierStockePath);
        
        String nomConverti = 
            nomStockage.substring(0, nomStockage.lastIndexOf(".")) + "." + typeApres;
        File fichierConverti = new File(uploadDir, nomConverti);
        
        // Effectuer la conversion selon le type
        if (typeAvant.equals("pdf") && typeApres.equals("docx")) {
            fichierConverti = convertPdfToWord(fichierStocke);
        } else if (typeAvant.equals("pdf") && typeApres.equals("xlsx")) {
            fichierConverti = convertPdfToExcel(fichierStocke);
        } else if (typeAvant.equals("docx") && typeApres.equals("pdf")) {
            fichierConverti = convertWordToPdf(fichierStocke);
        }
        
        Document doc = new Document();
        doc.setNomFichier(nomOriginal);
        doc.setTypeAvant(typeAvant);
        doc.setTypeApres(typeApres);
        doc.setNomFichierConverti(fichierConverti.getName());
        doc.setDateConversion(LocalDateTime.now());
        
        if (u != null) {
            doc.setUserId(u.getId());
        }
        
        return documentRepository.save(doc);
    }
    
    public List<Document> getDocumentsUtilisateur(int userId) {
        return documentRepository.findByUserIdOrderByDateConversionDesc(userId);  // ✅ CORRECTION LIGNE 173
    }
    
    private String getExtension(String nomFichier) {
        if (nomFichier == null) return "";
        int i = nomFichier.lastIndexOf('.');
        if (i > 0) return nomFichier.substring(i + 1).toLowerCase();
        return "";
    }
    
    private boolean isConversionAutorisee(String avant, String apres) {
        return (avant.equals("docx") && apres.equals("pdf")) ||
               (avant.equals("pdf") && apres.equals("docx")) ||
               (avant.equals("pdf") && apres.equals("xlsx"));
    }
}