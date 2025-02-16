package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Cette classe représente une alerte téléphonique contenant une liste de numéros
 * de téléphone des résidents concernés.
 */
@Getter
@Setter
@AllArgsConstructor
public class PhoneAlertDTO {

    private List<String> phoneNumbers; // Liste des numéros de téléphone des résidents

}