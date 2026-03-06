/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author SOS PC MULTISERVICES
 */

@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "nom_fichier")
    private String nomFichier;
    
    @Column(name = "type_avant")
    private String typeAvant;
    
    @Column(name = "type_apres")
    private String typeApres;
    
    @Column(name = "date_conversion")
    private LocalDateTime dateConversion;
    
    @Column(name = "NomFichierConverti")
    private String nomFichierConverti;
    
    public Document() {}
    
    public Document(Integer id, Integer userId, String nomFichier, String typeAvant,
                    String typeApres, LocalDateTime dateConversion, String nomFichierConverti) {
        this.id = id;
        this.userId = userId;
        this.nomFichier = nomFichier;
        this.typeAvant = typeAvant;
        this.typeApres = typeApres;
        this.dateConversion = dateConversion;
        this.nomFichierConverti = nomFichierConverti;
    }

     public Document(Integer userId, String nomFichier, String typeAvant,
                    String typeApres, String nomFichierConverti) {
        this.userId = userId;
        this.nomFichier = nomFichier;
        this.typeAvant = typeAvant;
        this.typeApres = typeApres;
        this.nomFichierConverti = nomFichierConverti;
    }
     
      public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public String getTypeAvant() { return typeAvant; }
    public void setTypeAvant(String typeAvant) { this.typeAvant = typeAvant; }

    public String getTypeApres() { return typeApres; }
    public void setTypeApres(String typeApres) { this.typeApres = typeApres; }

    public LocalDateTime getDateConversion() { return dateConversion; }
    public void setDateConversion(LocalDateTime dateConversion) { this.dateConversion = dateConversion; }

    public String getNomFichierConverti() { return nomFichierConverti; }
    public void setNomFichierConverti(String nomFichierConverti) { this.nomFichierConverti = nomFichierConverti; }
    
}
