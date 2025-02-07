package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FireStationCoverageDTO {

    private List<PersonInfo> persons; // Liste des informations des personnes couvertes
    private int numberOfAdults;       // Nombre d'adultes
    private int numberOfChildren;    // Nombre d'enfants

    @Getter
    @Setter
    @AllArgsConstructor
    // Classe interne pour stocker les informations de chaque personne
    public static class PersonInfo {
        private String firstName;
        private String lastName;
        private String address;
        private String phone;


    }
}