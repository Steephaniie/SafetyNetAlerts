package com.safetynet.safetynetalerts.dto;

import java.util.List;

public class PhoneAlertDTO {

    private List<String> phoneNumbers; // Liste des numéros de téléphone des résidents

    public PhoneAlertDTO(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}