package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.service.ChildAlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChildAlertController {

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    /**
     * Endpoint permettant de récupérer la liste des enfants et des membres du foyer pour une adresse.
     *
     * @param address L'adresse pour laquelle effectuer la recherche.
     * @return Un DTO contenant les enfants et autres membres du foyer.
     */
    @GetMapping("/childAlert")
    public ChildAlertDTO getChildrenAtAddress(@RequestParam String address) {
        return childAlertService.getChildrenAtAddress(address);
    }
}