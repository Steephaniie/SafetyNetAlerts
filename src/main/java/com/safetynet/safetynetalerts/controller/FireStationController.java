package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.service.FireStationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    /**
     * Ajout d'un mapping caserne/adresse.
     *
     * @param fireStation La caserne à ajouter.
     * @return Une réponse HTTP avec le statut approprié.
     */
    @PostMapping
    public ResponseEntity<String> addFireStation(@RequestBody FireStation fireStation) {
        fireStationService.addFireStation(fireStation);
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
    public ResponseEntity<String> updateFireStation(
            @RequestParam String address,
            @RequestParam String newStationNumber) {
        boolean updated = fireStationService.updateFireStation(address, newStationNumber);
        if (updated) {
            return ResponseEntity.ok("Caserne mise à jour avec succès.");
        } else {
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
    public ResponseEntity<String> deleteFireStation(@RequestParam String address) {
        boolean deleted = fireStationService.deleteFireStation(address);
        if (deleted) {
            return ResponseEntity.ok("Caserne supprimée avec succès.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupération de la liste de toutes les casernes.
     *
     * @return Une liste d'objets `FireStation`.
     */
    @GetMapping
    public ResponseEntity<List<FireStation>> getAllFireStations() {
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        return ResponseEntity.ok(fireStations);
    }
}