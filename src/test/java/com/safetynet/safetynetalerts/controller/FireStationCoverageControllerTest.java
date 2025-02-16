package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.service.FireStationCoverageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Classe de test pour le contrôleur FireStationCoverageController.
 * Cette classe vérifie les fonctionnalités liées à la couverture des casernes de pompiers.
 * Utilise MockMvc pour tester les comportements des endpoints REST associés.
 */
@WebMvcTest(FireStationCoverageController.class)
class FireStationCoverageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationCoverageService fireStationCoverageService;

    /**
     * Teste la méthode GET pour obtenir la couverture d'une caserne de pompiers.
     * Vérifie que la réponse contient le nombre correct d'adultes et d'enfants.
     *
     * @throws Exception en cas d'échec de la requête MockMvc.
     */
    @Test
    void testGetCoverageByStationNumber_shouldReturnCoverage() throws Exception {
        // Arrange : Prépare les données simulées nécessaires pour le test
        String stationNumber = "1";
        FireStationCoverageDTO mockResponse = new FireStationCoverageDTO();
        mockResponse.setNumberOfAdults(5);
        mockResponse.setNumberOfChildren(2);
        when(fireStationCoverageService.getCoverageByStationNumber(stationNumber)).thenReturn(mockResponse);

        // Act : Exécute une requête GET vers l'URL cible
        // Assert : Vérifie que le statut de la réponse est OK (200) et que les données sont correctes
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").value(5))
                .andExpect(jsonPath("$.numberOfChildren").value(2));
    }

    /**
     * Teste la méthode GET pour obtenir la couverture d'une caserne de pompiers.
     * Vérifie que la requête retourne une erreur BAD REQUEST (400) si le paramètre stationNumber est manquant.
     *
     * @throws Exception en cas d'échec de la requête MockMvc.
     */
    @Test
    void testGetCoverageByStationNumber_shouldReturnBadRequestWhenStationNumberMissing() throws Exception {
        // Arrange : Aucun paramètre n'est configuré pour la requête

        // Act : Exécute une requête GET avec des paramètres incomplets
        // Assert : Vérifie que le statut de la réponse est BAD REQUEST (400)
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Teste la méthode GET pour obtenir la couverture d'une caserne de pompiers.
     * Vérifie que la requête retourne une erreur NOT FOUND (404) si la caserne demandée n'existe pas.
     *
     * @throws Exception en cas d'échec de la requête MockMvc.
     */
    @Test
    void testGetCoverageByStationNumber_shouldReturn404WhenNotFound() throws Exception {
        // Arrange : Configure un stationNumber inexistant dans les données simulées
        String stationNumber = "999";
        when(fireStationCoverageService.getCoverageByStationNumber(stationNumber))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire station not found"));

        // Act : Exécute une requête GET vers l'URL cible avec un stationNumber inconnu
        // Assert : Vérifie que le statut de la réponse est NOT FOUND (404)
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isNotFound());
    }
}