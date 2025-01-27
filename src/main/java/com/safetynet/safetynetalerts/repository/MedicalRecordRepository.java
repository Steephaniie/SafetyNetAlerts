package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FichierJsonDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordRepository {
    private List<MedicalRecord> medicalrecords;

    public MedicalRecordRepository() {
        loadData();
    }

    private void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/resources/data.json");
            // Lire les données et les convertir en listes
            FichierJsonDTO fichierJsonDTO = objectMapper.readValue(jsonFile, FichierJsonDTO.class);
            medicalrecords=fichierJsonDTO.getMedicalrecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Ajouter un dossier médical
     * @param medicalRecord Le dossier médical à ajouter
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalrecords.add(medicalRecord);
    }

    /**
     * Mettre à jour un dossier médical existant
     * @param firstName Prénom de l'utilisateur
     * @param lastName Nom de famille de l'utilisateur
     * @param updatedMedicalRecord Les nouvelles informations du dossier médical
     * @return true si la mise à jour est réussie, false sinon
     */
    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        Optional<MedicalRecord> existingRecord = medicalrecords.stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(firstName)
                        && record.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingRecord.isPresent()) {
            int index = medicalrecords.indexOf(existingRecord.get());
            medicalrecords.set(index, updatedMedicalRecord);
            return true;
        }
        return false;
    }

    /**
     * Supprimer un dossier médical
     * @param firstName Prénom de l'utilisateur
     * @param lastName Nom de famille de l'utilisateur
     * @return true si la suppression est réussie, false sinon
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return medicalrecords.removeIf(record ->
                record.getFirstName().equalsIgnoreCase(firstName)
                        && record.getLastName().equalsIgnoreCase(lastName));
    }

    /**
     * Récupérer la liste de tous les dossiers médicaux.
     *
     * @return Liste des dossiers médicaux.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return medicalrecords;
    }
}

