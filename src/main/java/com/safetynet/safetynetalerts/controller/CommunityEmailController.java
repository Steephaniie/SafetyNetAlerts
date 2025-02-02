package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.service.CommunityEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/communityEmail")
    public ResponseEntity<CommunityEmailDTO> getEmailsByCity(@RequestParam("city") String city) {
        CommunityEmailDTO communityEmail = communityEmailService.getEmailsByCity(city);
        return ResponseEntity.ok(communityEmail);
    }
}