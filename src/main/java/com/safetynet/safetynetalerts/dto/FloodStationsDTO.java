package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FloodStationsDTO {

    private Map<String, List<HouseholdInfo>> householdsByAddress; // Adresses et leurs résidents

    @Getter
    @Setter
    @AllArgsConstructor
    // Classe interne pour représenter un résident d'un foyer
    public static class HouseholdInfo {
        private String firstName;        //prénom
        private String lastName;         // Nom
        private String phone;            // Numéro de téléphone
        private int age;                 // Âge
        private List<String> medications; // Médicaments
        private List<String> allergies;   // Allergies

    }
}