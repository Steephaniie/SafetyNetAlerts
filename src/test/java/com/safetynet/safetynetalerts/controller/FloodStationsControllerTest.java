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

/**
 * Classe de test pour le contrôleur FloodStationsController.
 * Vérifie les différents scénarios concernant le point d'accès /flood/stations.
 */
@WebMvcTest(FloodStationsController.class)
class FloodStationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FloodStationsService floodStationsService;

    private FloodStationsDTO mockResponse;

    @BeforeEach
    void setUp() {
        // Prépare les données de test avant chaque méthode
        mockResponse = new FloodStationsDTO();

        // Simule les données représentant householdsByAddress à renvoyer par le service
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

    /**
     * Vérifie que l'accès au point /flood/stations fonctionne correctement
     * lorsque des paramètres valides sont fournis.
     *
     * @throws Exception si la requête échoue.
     */
    @Test
    void testGetFloodStations_success() throws Exception {
        // Configuration du service mock pour retourner une réponse attendue
        Mockito.when(floodStationsService.getHouseholdsByStations(anyList()))
                .thenReturn(mockResponse);

        // Envoi d'une requête GET au point d'accès avec des paramètres valides
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérification que le code HTTP renvoyé est 200
                .andExpect(status().isOk())
                // Vérification que householdsByAddress est une structure de type Map
                .andExpect(jsonPath("$.householdsByAddress").isMap())
                // Vérification que l'adresse Address 1 contient une liste
                .andExpect(jsonPath("$.householdsByAddress['Address 1']").isArray())
                // Vérification des données du premier élément de la liste
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].firstName").value("John"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].lastName").value("Doe"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].phone").value("123-456"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].age").value(30))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].medications[0]").value("Med1"))
                .andExpect(jsonPath("$.householdsByAddress['Address 1'][0].allergies[0]").value("Allergy1"));
    }

    /**
     * Vérifie que l'accès au point /flood/stations retourne une erreur
     * lorsque le paramètre est manquant ou invalide.
     *
     * @throws Exception si la requête échoue.
     */
    @Test
    void testGetFloodStations_invalidParameter() throws Exception {
        // Simulation d'une requête avec un paramètre vide
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "") // Paramètre invalide (vide)
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérification que le code HTTP renvoyé est 400 (Bad Request)
                .andExpect(status().isBadRequest());
    }

    /**
     * Vérifie que l'accès au point /flood/stations retourne une erreur
     * 500 si une exception est levée par le service.
     *
     * @throws Exception si la requête échoue.
     */
    @Test
    void testGetFloodStations_serviceError() throws Exception {
        // Simulation de l'exception levée par le service FloodStationsService
        Mockito.when(floodStationsService.getHouseholdsByStations(anyList()))
                .thenThrow(new RuntimeException("Service Error"));

        // Envoi d'une requête GET et vérification de la gestion correcte de l'erreur
        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2")
                        .contentType(MediaType.APPLICATION_JSON))
                // Vérification que le code HTTP renvoyé est 500 (Internal Server Error)
                .andExpect(status().isInternalServerError());
    }
}
