package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.service.PhoneAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@Tag(name = "Phone Alert Controller", description = "Gestion des numéros de téléphone des résidents couverts par une caserne.")
public class PhoneAlertController {

    private final PhoneAlertService phoneAlertService;

    @Autowired
    public PhoneAlertController(PhoneAlertService phoneAlertService) {
        this.phoneAlertService = phoneAlertService;
    }

    /**
     * Endpoint pour récupérer les numéros de téléphone des résidents couverts par une caserne.
     *
     * @param firestation Le numéro de la caserne.
     * @return Une liste des numéros de téléphone en tant que DTO.
     */
    @GetMapping("/phoneAlert")
    @Operation(summary = "Récupérer les numéros de téléphone des résidents", description = "Retourne les numéros de téléphone de tous les résidents couverts par une caserne spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Numéros récupérés avec succès."),
            @ApiResponse(responseCode = "204", description = "Aucun contenu disponible, aucun numéro trouvé.")
    })
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam("firestation") String firestation) {
        PhoneAlertDTO phoneAlertDTO = phoneAlertService.getPhonesByFireStation(firestation);

        if (phoneAlertDTO.getPhoneNumbers().isEmpty()) {
            log.info ("api getPhoneAlert - aucun numéro trouvé");
            return ResponseEntity.noContent().build(); // Aucun numéro trouvé
        }
        log.info("api getPhoneAlert ok");
        return ResponseEntity.ok(phoneAlertDTO);
    }
}