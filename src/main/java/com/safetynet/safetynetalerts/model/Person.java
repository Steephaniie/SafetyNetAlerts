package com.safetynet.safetynetalerts.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Représente une personne avec des informations personnelles, de contact et d'adresse.
 * Cette classe encapsule des détails de base tels que le nom, l'adresse, la ville,
 * le code postal, le numéro de téléphone et l'email.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @NotBlank(message = "Le prénom ne peut pas être vide.")
    private String firstName;
    @NotBlank(message = "Le nom ne peut pas être vide.")
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}