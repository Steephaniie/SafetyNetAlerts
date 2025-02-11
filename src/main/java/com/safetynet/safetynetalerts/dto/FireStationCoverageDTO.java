package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


/**
 * DTO représentant les informations sur la couverture d'une caserne de pompiers,
 * y compris les détails des personnes couvertes et le nombre d'adultes et d'enfants protégés.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FireStationCoverageDTO {

    private List<PersonInfo> persons;
    private int numberOfAdults;
    private int numberOfChildren;

    /**
     * Classe interne représentant les informations d'une personne spécifique couverte
     * par la caserne de pompiers.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class PersonInfo {

        private String firstName;
        private String lastName;
        private String address;
        private String phone;
    }
}