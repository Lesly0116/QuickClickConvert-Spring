/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Lesly.ConvertirFichier.repository;

import com.Lesly.ConvertirFichier.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author SOS PC MULTISERVICES
 */

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    
      Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
    
}
