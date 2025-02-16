package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.service.FireStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/firestation")
@Tag(name = "FireStation Controller", description = "Gestion des associations entre casernes et adresses.")
@AllArgsConstructor
public class FireStationController {

    private final FireStationService fireStationService;

    /**
     * Ajout d'un mapping caserne/adresse.
     *
     * @param fireStation La caserne à ajouter.
     * @return Une réponse HTTP avec le statut approprié.
     */
    @PostMapping
    @Operation(summary = "Ajouter une nouvelle association caserne/adresse", description = "Permet d'ajouter une nouvelle correspondance entre une caserne et une adresse spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Association ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<String> addFireStation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails de la nouvelle correspondance caserne/adresse à ajouter.")
            @Valid @RequestBody FireStation fireStation) {
        fireStationService.addFireStation(fireStation);
        log.info("api addFireStation ok ");
        return ResponseEntity.ok("Caserne ajoutée avec succès.");
    }

    /**
     * Mise à jour du numéro de la caserne pour une adresse donnée.
     *
     * @param address          L'adresse à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return Une réponse HTTP avec le statut correspondant.
     */
    @PutMapping
    @Operation(summary = "Mettre à jour le numéro d'une caserne", description = "Modifie le numéro de caserne associé à une adresse donnée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Caserne mise à jour avec succès."),
            @ApiResponse(responseCode = "404", description = "Adresse introuvable.")
    })
    public ResponseEntity<String> updateFireStation(
            @Parameter(description = "Adresse à mettre à jour.")
            @RequestParam String address,
            @Parameter(description = "Nouveau numéro de caserne.")
            @RequestParam String newStationNumber) {
        boolean updated = fireStationService.updateFireStation(address, newStationNumber);
        if (updated) {
            log.info("api updateFireStation ok ");
            return ResponseEntity.ok("Caserne mise à jour avec succès.");
        } else {
            log.info("api updateFireStation ko - données de mise à jour non trouvées ");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Suppression d'un mapping caserne/adresse.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return Une réponse HTTP avec le statut correspondant.
     */
    @DeleteMapping
    @Operation(summary = "Supprimer une association caserne/adresse", description = "Supprime la correspondance d'une caserne pour une adresse spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Correspondance supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Adresse introuvable.")
    })
    public ResponseEntity<String> deleteFireStation(
            @Parameter(description = "Adresse de l'association caserne/adresse à supprimer.")
            @RequestParam String address) {
        boolean deleted = fireStationService.deleteFireStation(address);
        if (deleted) {
            log.info("api deleteFireStation ok ");
            return ResponseEntity.ok("Caserne supprimée avec succès.");
        } else {
            log.info("api deleteFireStation ko - donnée introuvable");
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupération de la liste de toutes les casernes.
     *
     * @return Une liste d'objets `FireStation`.
     */
    @GetMapping
    @Operation(summary = "Récupérer toutes les associations caserne/adresse", description = "Retourne une liste des correspondances entre casernes et adresses enregistrées.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès.")
    })
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        log.info("api getAllFireStations ok ");
        return ResponseEntity.ok(fireStations);
    }
}