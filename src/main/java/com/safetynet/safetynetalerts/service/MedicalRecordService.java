package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MedicalRecordService {
    
    private final MedicalRecordRepository medicalRecordRepository;

    /**
     * Ajouter un nouveau dossier médical.
     *
     * @param medicalRecord Le dossier médical à ajouter.
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        log.debug("Tentative d'ajout d'un dossier médical : {} {} - Date de naissance : {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName(), medicalRecord.getBirthdate());
        medicalRecordRepository.addMedicalRecord(medicalRecord);
        log.debug("Dossier médical ajouté avec succès : {} {} - Date de naissance : {}",
                medicalRecord.getFirstName(), medicalRecord.getLastName(), medicalRecord.getBirthdate());
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
        log.debug("Tentative de mise à jour du dossier médical pour : {} {} - Nouvelle date de naissance : {}",
                firstName, lastName, updatedMedicalRecord.getBirthdate());
        boolean isUpdated = medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
        if (isUpdated) {
            log.debug("Mise à jour réussie du dossier médical pour : {} {} - Nouvelle date de naissance : {}",
                    firstName, lastName, updatedMedicalRecord.getBirthdate());
        } else {
            log.debug("Échec de la mise à jour du dossier médical pour : {} {}", firstName, lastName);
        }
        return isUpdated;
    }

    /**
     * Supprimer un dossier médical.
     *
     * @param firstName Prénom du patient.
     * @param lastName  Nom du patient.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        log.debug("Tentative de suppression du dossier médical pour : {} {}", firstName, lastName);
        boolean isDeleted = medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
        if (isDeleted) {
            log.debug("Dossier médical supprimé avec succès : {} {}", firstName, lastName);
        } else {
            log.debug("Échec de la suppression du dossier médical pour : {} {}", firstName, lastName);
        }
        return isDeleted;
    }

    /**
     * Récupérer tous les dossiers médicaux.
     *
     * @return La liste des dossiers médicaux.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        log.debug("Début de la récupération de tous les dossiers médicaux.");
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        for (MedicalRecord record : medicalRecords) {
            log.debug("Dossier trouvé : {} {} - Date de naissance : {}",
                    record.getFirstName(), record.getLastName(), record.getBirthdate());
        }
        log.debug("Fin de récupération des dossiers médicaux. Nombre de dossiers trouvés : {}", medicalRecords.size());
        return medicalRecords;
    }
}