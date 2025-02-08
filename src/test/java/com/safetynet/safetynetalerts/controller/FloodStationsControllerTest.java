package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FloodStationsDTO;
import com.safetynet.safetynetalerts.service.FloodStationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FloodStationsController.class)
class FloodStationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FloodStationsService floodStationsService;

    private FloodStationsDTO mockResponse;

    @BeforeEach
    void setUp() {
        // Initialisation d'une réponse fictive pour les tests
        mockResponse = new FloodStationsDTO();
        // Simuler householdsByAddress avec des données fictives
        mockResponse.setHouseholdsByAddress(Map.of(
                "Address 1", Arrays.asList(
                        new FloodStationsDTO.HouseholdInfo("John", "Doe", "123-456", 30, Arrays.asList("Med1"), Arrays.asList("Allergy1")),
                        new FloodStationsDTO.HouseholdInfo("Jane", "Doe", "789-012", 25, Arrays.asList("Med2"), Arrays.asList("Allergy2"))
                ),
                "Address 2", Arrays.asList(
                        new FloodStationsDTO.HouseholdInfo("Alice", "Smith", "345-678", 40, Arrays.asList("Med3"), Arrays.asList("Allergy3"))
                )
        ));

    }

    @Test
    void testGetFloodStations_success() throws Exception {
        // Configurer la simulation de FloodStationsService
        Mockito.when(floodStationsService.getHouseholdsByStations(anyList()))
                .thenReturn(mockResponse);

        // Envoyer une requête GET avec des paramètres valides et vérifier le résultat
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le code HTTP 200
                .andExpect(jsonPath("$.householdsByAddress").isMap()) // Vérifie que householdsByAddress est bien un objet
                .andExpect(jsonPath("$.householdsByAddress['Address 1']").isArray()) // Vérifie que Address 1 contient une liste
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].firstName").value("John")) // Vérifie le premier élément
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].lastName").value("Doe"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].phone").value("123-456"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].age").value(30))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].medications[0]").value("Med1"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].allergies[0]").value("Allergy1"));
    }

    @Test
    void testGetFloodStations_invalidParameter() throws Exception {
        // Envoyer une requête GET avec des paramètres manquants ou invalides
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "") // Paramètre vide
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Vérifie le code HTTP 400
    }

    @Test
    void testGetFloodStations_serviceError() throws Exception {
        // Configurer FloodStationsService pour renvoyer une exception en simulant une erreur
        Mockito.when(floodStationsService.getHouseholdsByStations(anyList()))
                .thenThrow(new RuntimeException("Service Error"));

        // Envoyer une requête GET et vérifier que l'erreur est correctement propagée
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // Vérifie le code HTTP 500
    }
}
