package com.safetynet.safetynetalerts.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Représente une caserne de pompiers avec une adresse et un numéro.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FireStation {
    @NotBlank(message = "L'adresse ne peut pas être vide.")
    private String address; // Adresse de la station
    @NotBlank(message = "Le numéro de caserne ne peut pas être vide.")
    private String station; // Numéro de la station

}