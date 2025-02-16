package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Classe représentant l'enregistrement médical d'une personne.
 * Contient les informations personnelles et médicales d'un individu.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    @NotBlank(message = "Le prénom ne peut pas être vide.")
    private String firstName; // Prénom

    @NotBlank(message = "Le nom ne peut pas être vide.")
    private String lastName;  // Nom

    @NotNull(message = "La date de naissance est obligatoire.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    @JsonProperty("birthdate")
    private Date birthDate; // Date de naissance
    private List<String> allergies;
    private List<String> medications;
}