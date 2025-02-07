package com.safetynet.safetynetalerts.dto;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FichierJsonDTO {
    List<Person>  persons;
    List<FireStation> firestations;
    List<MedicalRecord> medicalrecords;
}
