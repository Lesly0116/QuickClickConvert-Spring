package com.Lesly.ConvertirFichier.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_creation", nullable = false)
    private Timestamp dateCreation;

    @Column(nullable = false)
    private String role;

    public Utilisateur() {
    }

    public Utilisateur(Integer id, String email, String password, Timestamp dateCreation, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dateCreation = dateCreation;
        this.role = role;
    }

    public Utilisateur(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.dateCreation = new Timestamp(System.currentTimeMillis());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
    }
}
