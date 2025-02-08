package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.service.ChildAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildAlertService childAlertService;

    private ChildAlertDTO mockChildAlertDTO;

    @BeforeEach
    void setUp() {
        // Crée un DTO simulé pour le test
        mockChildAlertDTO = new ChildAlertDTO();

        // Crée une liste d'objets ChildInfo pour les enfants
        mockChildAlertDTO.setChildren(List.of(
                new ChildAlertDTO.ChildInfo("John", "Doe", 10),
                new ChildAlertDTO.ChildInfo("Jane", "Doe", 8)
        ));

        // Crée une liste d'objets HouseholdMember pour les adultes
        mockChildAlertDTO.setOtherHouseholdMembers(List.of(
                new ChildAlertDTO.HouseholdMember("Jack", "Doe"),
                new ChildAlertDTO.HouseholdMember("Jill", "Doe")
        ));

    }

    @Test
    @DisplayName("Test succès - Récupération des enfants pour une adresse")
    void testGetChildrenAtAddressSuccess() throws Exception {
        // Simule la réponse du service
        String testAddress = "123 Test Street";
        when(childAlertService.getChildrenAtAddress(testAddress)).thenReturn(mockChildAlertDTO);

        // Envoie une requête GET et vérifie les résultats
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                        .param("address", testAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.children").isArray())
                .andExpect(jsonPath("$.children[0].firstName").value("John"))
                .andExpect(jsonPath("$.children[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.children[0].age").value(10))
                .andExpect(jsonPath("$.children[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.children[1].lastName").value("Doe"))
                .andExpect(jsonPath("$.children[1].age").value(8))
                .andExpect(jsonPath("$.otherHouseholdMembers").isArray())
                .andExpect(jsonPath("$.otherHouseholdMembers[0].firstName").value("Jack"))
                .andExpect(jsonPath("$.otherHouseholdMembers[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.otherHouseholdMembers[1].firstName").value("Jill"))
                .andExpect(jsonPath("$.otherHouseholdMembers[1].lastName").value("Doe"));
    }

    @Test
    @DisplayName("Test erreur - Adresse manquante")
    void testGetChildrenAtAddressMissingAddress() throws Exception {
        // Requête sans le paramètre obligatoire "address"
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test erreur - Service lance une exception")
    void testGetChildrenAtAddressServiceError() throws Exception {
        String testAddress = "123 Test Street";

        // Simule une exception lancée par le service
        when(childAlertService.getChildrenAtAddress(testAddress))
                .thenThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur interne du serveur"));

        // Requête et vérification du statut 500
        mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                        .param("address", testAddress))
                .andExpect(status().isInternalServerError());
    }
}