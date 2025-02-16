package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.service.ChildAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Child Alert Controller", description = "Gestion des alertes liées aux enfants pour une adresse donnée.")
@AllArgsConstructor
public class ChildAlertController {

    private final ChildAlertService childAlertService;

    /**
     * Endpoint permettant de récupérer la liste des enfants et des membres du foyer pour une adresse.
     *
     * @param address L'adresse pour laquelle effectuer la recherche.
     * @return Un DTO contenant les enfants et autres membres du foyer.
     */
    @GetMapping("/childAlert")
    @Operation(summary = "Récupérer la liste des enfants et des membres du foyer",
            description = "Retourne une liste contenant les enfants vivant à l'adresse spécifiée ainsi que les autres membres du foyer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informations récupérées avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide (ex : adresse manquante ou incorrecte)."),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur.")
    })
    public ChildAlertDTO getChildrenAtAddress(@RequestParam String address) {
        log.info("api getChildrenAtAddress ok");
        return childAlertService.getChildrenAtAddress(address);
    }
}