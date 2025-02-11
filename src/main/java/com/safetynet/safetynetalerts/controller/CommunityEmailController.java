package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.service.CommunityEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * Point d'entrée API pour récupérer les emails par ville.
     *
     * @param city La ville pour laquelle les emails doivent être retournés.
     * @return Une réponse contenant un DTO avec la ville et les emails.
     */
    @Operation(summary = "Récupérer les emails par ville", description = "Renvoie les emails des résidents d'une ville spécifique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Les emails ont été récupérés avec succès."),
            @ApiResponse(responseCode = "400", description = "La requête est invalide ou les paramètres sont manquants.")
    })
    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmailDTO> getEmailsByCity(
            @Parameter(description = "La ville pour laquelle récupérer les emails.")
            @RequestParam("city") String city) {
        CommunityEmailDTO communityEmail = communityEmailService.getEmailsByCity(city);
        log.info("api getEmailsByCity ok ");
        return ResponseEntity.ok(communityEmail);
    }
}