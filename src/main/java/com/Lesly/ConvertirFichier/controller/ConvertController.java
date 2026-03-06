/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.controller;

import com.Lesly.ConvertirFichier.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.HttpSession;
import com.Lesly.ConvertirFichier.service.ServiceDocument;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author jeanl
 */

@Controller
@RequestMapping("/conversion")
public class ConvertController {
    
   private final ServiceDocument serviceDocument;
    
    @Value("${upload.directory}")
    private String uploadDir;
    
    public ConvertController(ServiceDocument serviceDocument) {
        this.serviceDocument = serviceDocument;
    }
    
    @GetMapping("/page")
    public String afficherPageConversion(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateurConnecte");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("utilisateur", user);
        
        // Récupérer l'historique des conversions de l'utilisateur
        try {
            List<Document> historique = serviceDocument.getDocumentsUtilisateur(user.getId());
            model.addAttribute("historique", historique);
        } catch (Exception e) {
            model.addAttribute("error", "Impossible de charger l'historique");
        }
        
        return "pageConversion";
    }
    
    @PostMapping("/convertir")
public String convertirFichier(
        @RequestParam("fichier") MultipartFile fichier,
        @RequestParam("format") String format,
        HttpSession session,
        Model model) {
    
    try {
        // Vérification basique
        if (fichier.isEmpty()) {
            model.addAttribute("error", "Veuillez sélectionner un fichier");
            return "redirect:/conversion/page";
        }
        
        if (fichier.getSize() > 50 * 1024 * 1024) {
            model.addAttribute("error", "Le fichier est trop volumineux (max 50MB)");
            return "redirect:/conversion/page";
        }
        
        Document doc = serviceDocument.convertir(session, fichier, format);
        model.addAttribute("success", "Conversion réussie !");
        model.addAttribute("document", doc);
        
    } catch (Exception e) {
        model.addAttribute("error", "Erreur de conversion : " + e.getMessage());
        e.printStackTrace(); // Pour voir l'erreur dans la console
    }
    
    return "redirect:/conversion/page";
}
    
    @GetMapping("/telecharger/{nomFichier}")
    public ResponseEntity<Resource> telechargerFichier(@PathVariable String nomFichier) {
        try {
            Path cheminFichier = Paths.get(uploadDir).resolve(nomFichier);
            Resource resource = new UrlResource(cheminFichier.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/historique")
    public String voirHistorique(HttpSession session, Model model) {
        Utilisateur user = (Utilisateur) session.getAttribute("utilisateurConnecte");
        
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("utilisateur",user);
        
        try {
            List<Document> historique = serviceDocument.getDocumentsUtilisateur(user.getId());
            model.addAttribute("historique", historique);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de l'historique");
        }
        
        return "historique";
    }
    
}
