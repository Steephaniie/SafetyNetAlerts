package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/medicalRecord")
@Tag(name = "MedicalRecordController", description = "Gestion des dossiers médicaux.")
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
    @Operation(summary = "Ajouter un nouveau dossier médical", description = "Ajoute un dossier médical pour une personne donnée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dossier médical ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide. Vérifiez les données envoyées.")
    })
    public ResponseEntity<String> addMedicalRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails du dossier médical à ajouter.")
            @Valid @RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.addMedicalRecord(medicalRecord);
        log.info("api addMedicalRecord ok");
        return ResponseEntity.status(HttpStatus.CREATED).body("Dossier médical ajouté avec succès.");
    }

    /**
     * Mettre à jour un dossier médical existant.
     *
     * @param firstName            Prénom de la personne.
     * @param lastName             Nom de la personne.
     * @param updatedMedicalRecord Les nouvelles informations du dossier.
     * @return Une réponse HTTP indiquant le succès ou l'échec de l'opération.
     */
    @PutMapping("/{firstName}/{lastName}")
    @Operation(summary = "Mettre à jour un dossier médical", description = "Mise à jour des informations pour un dossier médical existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical mis à jour avec succès."),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé."),
    })
    public ResponseEntity<String> updateMedicalRecord(
            @Parameter(description = "Prénom de la personne.") @PathVariable String firstName,
            @Parameter(description = "Nom de famille de la personne.") @PathVariable String lastName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails mis à jour du dossier médical.")
            @RequestBody MedicalRecord updatedMedicalRecord) {
        boolean isUpdated = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
        if (isUpdated) {
            log.info("api updateMedicalRecord ok");
            return ResponseEntity.ok("Dossier médical mis à jour avec succès.");
        } else {
            log.info ("api updateMedicalRecord ko - dossier non trouvé pour mise à jour");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dossier médical non trouvé pour mise à jour.");
        }
    }

    /**
     * Supprimer un dossier médical.
     *
     * @param firstName Prénom de la personne.
     * @param lastName  Nom de la personne.
     * @return Une réponse HTTP indiquant le succès ou l'échec de la suppression.
     */
    @DeleteMapping("/{firstName}/{lastName}")
    @Operation(summary = "Supprimer un dossier médical", description = "Supprime un dossier médical existant en fonction du prénom et du nom.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dossier médical supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Dossier médical non trouvé."),
    })
    public ResponseEntity<String> deleteMedicalRecord(
            @Parameter(description = "Prénom de la personne.") @PathVariable String firstName,
            @Parameter(description = "Nom de famille de la personne.") @PathVariable String lastName) {
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
        if (isDeleted) {
           log.info("api deleteMedicalRecord ok");
            return ResponseEntity.ok("Dossier médical supprimé avec succès.");
        } else {
            log.info( "api deleteMedicalRecord ko - dossier médical non trouvé pour suppression ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dossier médical non trouvé pour suppression.");
        }
    }

    /**
     * Récupérer tous les dossiers médicaux.
     *
     * @return Une réponse HTTP contenant la liste de tous les dossiers médicaux.
     */
    @GetMapping
    @Operation(summary = "Récupérer tous les dossiers médicaux", description = "Retourne la liste complète des dossiers médicaux enregistrés.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès.")
    })
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();
        log.info("api getAllMedicalRecords ok");
        return ResponseEntity.ok(medicalRecords);
    }
}