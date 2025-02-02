package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.service.FireStationCoverageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FireStationCoverageController {

    private final FireStationCoverageService fireStationCoverageService;

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
    public FireStationCoverageDTO getCoverageByStationNumber(
            @RequestParam("stationNumber") String stationNumber
    ) {
        return fireStationCoverageService.getCoverageByStationNumber(stationNumber);
    }
}