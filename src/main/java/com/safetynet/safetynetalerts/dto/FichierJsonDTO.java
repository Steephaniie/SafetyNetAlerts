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
 * Objet de transfert de données (DTO) représentant la structure JSON utilisée dans l'application pour stocker les données.
 * Cette classe contient des listes de personnes, de casernes de pompiers et de dossiers médicaux provenant du fichier JSON.
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
