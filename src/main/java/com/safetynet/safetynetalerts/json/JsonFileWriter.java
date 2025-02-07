package com.safetynet.safetynetalerts.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FichierJsonDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
@Getter
@Slf4j
@Repository
public class JsonFileWriter {
    private static final String JSON_FILE_PATH = "src/main/resources/data.json";

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    // Constructeur
    public JsonFileWriter() {
        loadData();
    }

    // chargement des données depuis le fichier Json
    private void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            // Lire les données et les convertir en listes
            FichierJsonDTO fichierJsonDTO = objectMapper.readValue(jsonFile, FichierJsonDTO.class);
            persons = fichierJsonDTO.getPersons();
            firestations = fichierJsonDTO.getFirestations();
            medicalrecords = fichierJsonDTO.getMedicalrecords();
        } catch (IOException e) {
            log.error ("JsonFileWriter - erreur lors du chargement des données");
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        FichierJsonDTO fichierJsonDTO = new FichierJsonDTO();
        fichierJsonDTO.setPersons(persons);
        fichierJsonDTO.setFirestations(firestations);
        fichierJsonDTO.setMedicalrecords(medicalrecords);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_FILE_PATH), fichierJsonDTO);
        } catch (IOException e) {
            log.error("writeToFile - erreur lors de la sauvegarde dans le fichier JSON");
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde dans le fichier JSON.");
        }
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
        writeToFile();
    }

    public void setFirestations(List<FireStation> firestations) {
        this.firestations = firestations;
        writeToFile();
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
        writeToFile();
    }
}
