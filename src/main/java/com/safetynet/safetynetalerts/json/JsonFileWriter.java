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


/**
 * Cette classe permet de lire et écrire des données dans un fichier JSON.
 * Elle sert à charger les données initiales et à les mettre à jour en fonction des entités de l'application.
 */
@Getter
@Slf4j
@Repository
public class JsonFileWriter {

    private static final String JSON_FILE_PATH = "src/main/resources/data.json";
    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecord> medicalrecords;

    // Constructeur par défaut qui initialise l'objet en chargeant les données du fichier JSON.
    public JsonFileWriter() {
        loadData();
    }


    /**
     * Charge les données depuis le fichier JSON et les stocke dans les listes correspondantes.
     * En cas d'erreur de lecture, un message d'erreur est enregistré dans les logs.
     */
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

    /**
     * Écrit les données actuelles des listes de l'application dans le fichier JSON.
     * En cas d'erreur d'écriture, une exception est lancée et un message d'erreur est enregistré dans les logs.
     */
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

    /**
     * Met à jour la liste des personnes et sauvegarde les modifications dans le fichier JSON.
     *
     * @param persons nouvelle liste des personnes à enregistrer.
     */
    public void setPersons(List<Person> persons) {
        this.persons = persons;
        writeToFile();
    }

    /**
     * Met à jour la liste des casernes de pompiers et sauvegarde les modifications dans le fichier JSON.
     *
     * @param firestations nouvelle liste des casernes de pompiers à enregistrer.
     */
    public void setFirestations(List<FireStation> firestations) {
        this.firestations = firestations;
        writeToFile();
    }

    /**
     * Met à jour la liste des dossiers médicaux et sauvegarde les modifications dans le fichier JSON.
     *
     * @param medicalrecords nouvelle liste des dossiers médicaux à enregistrer.
     */
    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
        writeToFile();
    }
}
