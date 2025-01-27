package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /**
     * Ajouter un nouveau dossier médical.
     *
     * @param medicalRecord Le dossier médical à ajouter.
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordRepository.addMedicalRecord(medicalRecord);
    }

    /**
     * Mettre à jour un dossier médical existant.
     *
     * @param firstName            Prénom du patient.
     * @param lastName             Nom du patient.
     * @param updatedMedicalRecord Les nouvelles informations du dossier médical.
     * @return true si la mise à jour a réussi, false sinon.
     */
    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        return medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
    }

    /**
     * Supprimer un dossier médical.
     *
     * @param firstName Prénom du patient.
     * @param lastName  Nom du patient.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
    }

    /**
     * Récupérer tous les dossiers médicaux.
     *
     * @return La liste des dossiers médicaux.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.getMedicalRecords();
    }
}