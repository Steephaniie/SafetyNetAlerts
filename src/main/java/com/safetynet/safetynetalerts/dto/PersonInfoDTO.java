package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


/**
 * Représente les informations personnelles d'une personne pour les alertes.
 */
@Getter
@Setter
@AllArgsConstructor
public class PersonInfoDTO {

    private String firstName; // Prénom
    private String lastName;  // Nom
    private String address;   // Adresse
    private int age;          // Âge
    private String email;     // Email
    private List<String> medications; // Médicaments et posologie
    private List<String> allergies;   // Allergies
}
