package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.service.PersonInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@Tag(name = "Person Info Controller", description = "Gestion des informations des habitants par nom de famille.")
public class PersonInfoController {

    private final PersonInfoService personInfoService;

    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    /**
     * Endpoint pour récupérer les informations sur les personnes par leur nom.
     *
     * @param lastName Nom de famille des personnes recherchées.
     * @return Une liste de DTO contenant les informations des habitants avec ce nom.
     */
    @GetMapping("/personInfo")
    @Operation(summary = "Récupérer les informations d'une personne par son nom de famille",
            description = "Retourne une liste contenant les informations des habitants correspondant au nom de famille fourni.")
    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès.")
    public List<PersonInfoDTO> getPersonInfo(@RequestParam("lastName") String lastName) {
       log.info("api getPersonInfo ok");
        return personInfoService.getPersonInfoByLastName(lastName);
    }
}