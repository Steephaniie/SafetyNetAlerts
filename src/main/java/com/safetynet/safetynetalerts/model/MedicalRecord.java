package com.safetynet.safetynetalerts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.safetynet.safetynetalerts.json.CustomDateDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecord {

    @NotBlank (message = "Le prénom ne peut pas être vide.")
    private String firstName; // Prénom

    @NotBlank(message = "Le nom ne peut pas être vide.")
    private String lastName;  // Nom

    @NotNull(message = "La date de naissance est obligatoire.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", timezone = "UTC")
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @JsonProperty("birthDate")
    private Date birthDate; // Date de naissance

    private List<String> allergies;
    private List<String> medications;
}