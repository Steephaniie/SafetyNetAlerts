package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.service.PhoneAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public ResponseEntity<PhoneAlertDTO> getPhoneAlert(@RequestParam("firestation") String firestation) {
        PhoneAlertDTO phoneAlertDTO = phoneAlertService.getPhonesByFireStation(firestation);

        if (phoneAlertDTO.getPhoneNumbers().isEmpty()) {
            return ResponseEntity.noContent().build(); // Aucun numéro trouvé
        }

        return ResponseEntity.ok(phoneAlertDTO);
    }
}