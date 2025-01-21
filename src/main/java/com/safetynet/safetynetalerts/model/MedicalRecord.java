package com.safetynet.safetynetalerts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique pour chaque enregistrement médical

    private String firstName; // Prénom
    private String lastName;  // Nom
    private LocalDate birthdate; // Date de naissance
    @ElementCollection
    private List<String> allergies;
    @ElementCollection
    private List<String> medications;
}