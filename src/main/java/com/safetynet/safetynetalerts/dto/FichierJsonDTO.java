package com.safetynet.safetynetalerts.dto;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the JSON structure used in the application for storing data.
 * This class contains lists of persons, fire stations, and medical records from the JSON file.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FichierJsonDTO {

    List<Person> persons;
    List<FireStation> firestations;
    List<MedicalRecord> medicalrecords;
}
