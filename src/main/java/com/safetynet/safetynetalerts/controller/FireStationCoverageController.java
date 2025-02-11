package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.service.FireStationCoverageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@Tag(name = "FireStationCoverage Controller", description = "Gestion de la couverture des casernes de pompiers.")
public class FireStationCoverageController {

    private final FireStationCoverageService fireStationCoverageService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    public FireStationCoverageController(FireStationCoverageService fireStationCoverageService) {
        this.fireStationCoverageService = fireStationCoverageService;
    }

    /**
     * Endpoint pour récupérer la couverture d'une caserne de pompiers.
     *
     * @param stationNumber Numéro de la caserne.
     * @return Les informations des habitants couverts (nom, adresse, téléphone) + décompte adultes/enfants.
     */
    @GetMapping(value = "/firestation", params = "stationNumber")
    @Operation(summary = "Récupérer la couverture d'une caserne de pompiers",
            description = "Retourne les informations des habitants couverts par une caserne de pompiers : nom, adresse, téléphone, ainsi que le décompte des adultes et enfants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Données récupérées avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Ressource non trouvée.")
    })
    public FireStationCoverageDTO getCoverageByStationNumber(@RequestParam("stationNumber") String stationNumber ) {
        log.info("api getCoverageByStationNumber ok");
        return fireStationCoverageService.getCoverageByStationNumber(stationNumber);
    }
}