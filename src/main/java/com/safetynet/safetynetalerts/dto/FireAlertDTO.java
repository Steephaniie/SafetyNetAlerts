package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FireAlertDTO {

    private String fireStationNumber; // Numéro de la caserne de pompiers
    private List<ResidentInfo> residents; // Liste des résidents vivant à l'adresse


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    // Classe interne pour représenter les informations de chaque résident
    public static class ResidentInfo {
        private String firstName;       // Prénom
        private String lastName;        // Nom
        private String phone;           // Numéro de téléphone
        private int age;                // Âge
        private List<String> medications; // Médicaments
        private List<String> allergies;   // Allergies

    }
}