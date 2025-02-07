package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class CommunityEmailDTO {

    private String city; // Ville pour laquelle les emails sont listés
    private List<String> emails; // Liste des adresses email

    // Constructeurs
    public CommunityEmailDTO() {
    }

    @Override
    public String toString() {
        return "CommunityEmailDTO{" +
                "city='" + city + '\'' +
                ", emails=" + emails +
                '}';
    }
}