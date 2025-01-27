package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Ajouter un nouveau dossier médical.
     *
     * @param medicalRecord Le dossier médical à ajouter.
     * @return Une réponse HTTP avec un message de confirmation.
     */
    @PostMapping
    public ResponseEntity<String> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dossier médical ajouté avec succès.");
    }

    /**
     * Mettre à jour un dossier médical existant.
     *
     * @param firstName Prénom de la personne.
     * @param lastName Nom de la personne.
     * @param updatedMedicalRecord Les nouvelles informations du dossier.
     * @return Une réponse HTTP indiquant le succès ou l'échec de l'opération.
     */
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> updateMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody MedicalRecord updatedMedicalRecord) {
        boolean isUpdated = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
        if (isUpdated) {
            return ResponseEntity.ok("Dossier médical mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dossier médical non trouvé pour mise à jour.");
        }
    }

    /**
     * Supprimer un dossier médical.
     *
     * @param firstName Prénom de la personne.
     * @param lastName Nom de la personne.
     * @return Une réponse HTTP indiquant le succès ou l'échec de la suppression.
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deleteMedicalRecord(
            @PathVariable String firstName,
            @PathVariable String lastName) {
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (isDeleted) {
            return ResponseEntity.ok("Dossier médical supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dossier médical non trouvé pour suppression.");
        }
    }

    /**
     * Récupérer tous les dossiers médicaux.
     *
     * @return Une réponse HTTP contenant la liste de tous les dossiers médicaux.
     */
    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        return ResponseEntity.ok(medicalRecords);
    }
}