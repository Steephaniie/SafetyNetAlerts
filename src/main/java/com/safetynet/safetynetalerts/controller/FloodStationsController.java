package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FloodStationsDTO;
import com.safetynet.safetynetalerts.service.FloodStationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
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
    public FloodStationsDTO getFloodStations(@RequestParam("stations") String stations) {
        // Convertir la liste des stations en un tableau
        List<String> stationNumbers = Arrays.asList(stations.split(","));
        return floodStationsService.getHouseholdsByStations(stationNumbers);
    }
}