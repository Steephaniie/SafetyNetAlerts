package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Cette classe représente un Data Transfer Object (DTO) utilisé pour contenir les adresses email
 * associées à une ville spécifique.
 */
@Getter
@Setter
@AllArgsConstructor
public class CommunityEmailDTO {

    private String city;
    private List<String> emails;

    /**
     * Constructeur par défaut.
     */
    public CommunityEmailDTO() {
    }

    /**
     * Méthode qui retourne une représentation sous forme de chaîne de caractères
     * de l'objet CommunityEmailDTO.
     *
     * @return Représentation sous forme de chaîne de l'objet CommunityEmailDTO.
     */
    @Override
    public String toString() {
        return "CommunityEmailDTO{" +
                "city='" + city + '\'' +
                ", emails=" + emails +
                '}';
    }
}