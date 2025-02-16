package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * DTO utilisé pour représenter une alerte concernant les enfants vivant à une adresse spécifique,
 * avec une liste des enfants et des autres membres du foyer.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildAlertDTO {

    private List<ChildInfo> children;
    private List<HouseholdMember> otherHouseholdMembers;

    /**
     * Classe interne représentant les informations d'un enfant vivant à l'adresse.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ChildInfo {
        private String firstName;
        private String lastName;
        private int age;
    }

    /**
     * Classe interne représentant un autre membre du foyer.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class HouseholdMember {

        private String firstName;
        private String lastName;
    }
}