/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Lesly.ConvertirFichier.repository;

import com.Lesly.ConvertirFichier.model.Document;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author jeanl
 */

@Repository
public interface DocumentsRepository extends JpaRepository<Document, Integer> {
    
    List<Document> findByUserId (Integer user_id);
    
    List<Document> findByUserIdOrderByDateConversionDesc(Integer userId);
    
    List<Document> findByDateConversion(LocalDateTime dateConversion);
    
     List<Document> findByUserIdIsNullOrderByDateConversionDesc();
    
     Optional<Document> findByNomFichierConverti(String nomFichierConverti);
    
    Optional<Document> findFirstByUserIdOrderByDateConversionDesc(Integer userId);
    
}
