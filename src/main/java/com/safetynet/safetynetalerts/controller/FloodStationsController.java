package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FloodStationsDTO;
import com.safetynet.safetynetalerts.service.FloodStationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
@Slf4j
@RestController
@Tag(name = "Flood Stations Controller", description = "Gestion des foyers desservis par les casernes.")
public class FloodStationsController {

    private final FloodStationsService floodStationsService;

    public FloodStationsController(FloodStationsService floodStationsService) {
        this.floodStationsService = floodStationsService;
    }

    /**
     * Endpoint pour récupérer les foyers desservis par une liste de casernes.
     *
     * @param stations Liste des numéros de casernes.
     * @return Informations des foyers regroupés par adresse.
     */
    @GetMapping("/flood/stations")
    @Operation(summary = "Récupérer les foyers par caserne",
            description = "Retourne les informations des foyers classées par adresse pour chaque caserne spécifiée.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Données récupérées avec succès."),
            @ApiResponse(responseCode = "400", description = "Paramètre invalide : Liste des numéros de casernes.")
    })
    public FloodStationsDTO getFloodStations(
            @Parameter(description = "Liste des numéros de casernes, séparés par des virgules.")
            @RequestParam("stations") String stations) {
        // Convertir la liste des stations en un tableau
        List<String> stationNumbers = Arrays.asList(stations.split(","));
        log.info("api getFloodStations ok");
        return floodStationsService.getHouseholdsByStations(stationNumbers);
    }
}