package com.safetynet.safetynetalerts.dto;

import java.util.List;

public class CommunityEmailDTO {

    private String city; // Ville pour laquelle les emails sont listés
    private List<String> emails; // Liste des adresses email

    // Constructeurs
    public CommunityEmailDTO() {
    }

    public CommunityEmailDTO(String city, List<String> emails) {
        this.city = city;
        this.emails = emails;
    }

    // Getters et Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return "CommunityEmailDTO{" +
                "city='" + city + '\'' +
                ", emails=" + emails +
                '}';
    }
}