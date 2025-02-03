package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.service.FireAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Fire Alert Controller", description = "Point d'entrée pour les alertes incendies.")
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
    @Operation(summary = "Récupérer les informations d'alerte incendie.",
            description = "Retourne les informations des habitants et de la caserne de pompiers associés à une adresse donnée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations récupérées avec succès."),
            @ApiResponse(responseCode = "404", description = "Adresse non trouvée.")
    })
    public ResponseEntity<FireAlertDTO> getFireAlert(@RequestParam String address) {
        FireAlertDTO fireAlertDTO = fireAlertService.getFireAlertByAddress(address);

        if (fireAlertDTO.getResidents().isEmpty() && fireAlertDTO.getFireStationNumber() == null) {
            return ResponseEntity.notFound().build(); // Adresse non trouvée
        }
        return ResponseEntity.ok(fireAlertDTO);
    }
}