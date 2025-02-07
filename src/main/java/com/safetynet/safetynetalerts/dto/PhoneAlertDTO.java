package com.safetynet.safetynetalerts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PhoneAlertDTO {

    private List<String> phoneNumbers; // Liste des numéros de téléphone des résidents

    }