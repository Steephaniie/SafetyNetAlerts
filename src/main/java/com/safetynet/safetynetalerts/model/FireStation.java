package com.safetynet.safetynetalerts.model;

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

    private String address; // Adresse de la station
    private String station; // Numéro de la station

}