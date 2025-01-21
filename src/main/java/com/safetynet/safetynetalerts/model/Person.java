package com.safetynet.safetynetalerts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique pour l'entité

    private String firstName; // Prénom
    private String lastName;  // Nom
    private String address;   // Adresse
    private String city;      // Ville
    private String zip;       // Code postal
    private String phone;     // Téléphone
    private String email;     // Email
}