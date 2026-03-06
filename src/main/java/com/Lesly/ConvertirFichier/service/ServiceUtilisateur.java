/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.service;

import com.Lesly.ConvertirFichier.model.Utilisateur;
import com.Lesly.ConvertirFichier.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author SOS PC MULTISERVICES
 */
@Service
public class ServiceUtilisateur {
    
    private final UtilisateurRepository utilisateurRepository;
    
    public ServiceUtilisateur(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
    
    @Transactional
    public void inscrire(String email, String password) throws Exception {

        if (utilisateurRepository.existsByEmail(email)) {
            throw new Exception("Cet email a déjà été utilisé !");
        }

        Utilisateur utilisateur = new Utilisateur(email, password, "USER");
        utilisateurRepository.save(utilisateur);
    }
    
    @Transactional(readOnly = true)
    public Utilisateur authentifier(String email, String password) throws Exception {

        Utilisateur utilisateur = utilisateurRepository
                .findByEmailAndPassword(email, password)
                .orElse(null);

        if (utilisateur == null) {
            throw new Exception("Email ou mot de passe incorrect !");
        }

        return utilisateur;
    }
    
}
