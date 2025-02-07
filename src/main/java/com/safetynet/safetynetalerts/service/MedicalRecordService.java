package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MedicalRecordService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

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
        logger.debug("Début de l'ajout d'un nouveau dossier médical : {}", medicalRecord);
        medicalRecordRepository.addMedicalRecord(medicalRecord);
        logger.debug("Fin de l'ajout du dossier médical : {}", medicalRecord);
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
        logger.debug("Début de la mise à jour du dossier médical pour : {}, {}", firstName, lastName);
        boolean isUpdated = medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
        if (isUpdated) {
            logger.debug("Mise à jour réussie du dossier médical pour : {}, {}", firstName, lastName);
        } else {
            logger.debug("Échec de la mise à jour du dossier médical pour : {}, {}", firstName, lastName);
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
        logger.debug("Début de la suppression du dossier médical pour : {}, {}", firstName, lastName);
        boolean isDeleted = medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
        if (isDeleted) {
            logger.debug("Suppression réussie du dossier médical pour : {}, {}", firstName, lastName);
        } else {
            logger.debug("Échec de la suppression du dossier médical pour : {}, {}", firstName, lastName);
        }
        return isDeleted;
    }

    /**
     * Récupérer tous les dossiers médicaux.
     *
     * @return La liste des dossiers médicaux.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        logger.debug("Début de la récupération de tous les dossiers médicaux.");
        List<MedicalRecord> medicalRecords = medicalRecordRepository.getMedicalRecords();
        logger.debug("Fin de récupération des dossiers médicaux. Nombre de dossiers trouvés : {}", medicalRecords.size());
        return medicalRecords;
    }
}