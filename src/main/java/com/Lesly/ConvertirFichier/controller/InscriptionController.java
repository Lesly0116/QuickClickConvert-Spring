/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.controller;

import com.Lesly.ConvertirFichier.model.Utilisateur;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import com.Lesly.ConvertirFichier.service.ServiceUtilisateur;

/**
 *
 * @author jeanl
 */

@Controller
public class InscriptionController {
    
    private final ServiceUtilisateur service;
    
    public InscriptionController(ServiceUtilisateur service) {
    
        this.service = service;
    
    }
    
    @GetMapping("/inscription")
    public String afficherFormulaireInscription() {
        
        return "inscription";
        
    }
    
    @PostMapping("/inscription")
    public String inscrire(
       @RequestParam("email") String email,
       @RequestParam("password") String password,
       Model model) {

    if (email == null || email.trim().isEmpty()) {
        model.addAttribute("error", "L'email est obligatoire");
        return "inscription";
    }
    
    if (password == null || password.length() < 6) {
        model.addAttribute("error", "Le mot de passe doit contenir au moins 6 caractères");
        return "inscription";
    }
    
    try {
        service.inscrire(email, password);
        return "redirect:/login?inscription=success";
    } catch(Exception e) {
        model.addAttribute("error", e.getMessage());
        return "inscription";
    }
    }
    
}
