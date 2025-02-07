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
public class ChildAlertDTO {
    private List<ChildInfo> children; // Liste des enfants habitant à l'adresse
    private List<HouseholdMember> otherHouseholdMembers; // Liste des autres membres du foyer

    @Getter
    @Setter
    @AllArgsConstructor
    // Classe interne pour représenter les informations sur les enfants
    public static class ChildInfo {
        private String firstName;
        private String lastName;
        private int age;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    // Classe interne pour représenter les autres membres du foyer
    public static class HouseholdMember {
        private String firstName;
        private String lastName;
    }
}