package com.safetynet.safetynetalerts.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private String firstName; // Prénom
    private String lastName;  // Nom
    private String address;   // Adresse
    private String city;      // Ville
    private String zip;       // Code postal
    private String phone;     // Téléphone
    private String email;     // Email
}