package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.service.FireAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireAlertController {

    private final FireAlertService fireAlertService;

    public FireAlertController(FireAlertService fireAlertService) {
        this.fireAlertService = fireAlertService;
    }

    /**
     * Point d'entrée de l'API pour l'alerte incendie.
     *
     * @param address L'adresse pour laquelle récupérer les informations.
     * @return Un DTO FireAlertDTO contenant les détails des habitants et de la caserne de pompiers.
     */
    @GetMapping("/fire")
    public ResponseEntity<FireAlertDTO> getFireAlert(@RequestParam String address) {
        FireAlertDTO fireAlertDTO = fireAlertService.getFireAlertByAddress(address);

        if (fireAlertDTO.getResidents().isEmpty() && fireAlertDTO.getFireStationNumber() == null) {
            return ResponseEntity.notFound().build(); // Adresse non trouvée
        }
        return ResponseEntity.ok(fireAlertDTO);
    }
}