package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.service.PersonInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
    public List<PersonInfoDTO> getPersonInfo(@RequestParam("lastName") String lastName) {
        return personInfoService.getPersonInfoByLastName(lastName);
    }
}