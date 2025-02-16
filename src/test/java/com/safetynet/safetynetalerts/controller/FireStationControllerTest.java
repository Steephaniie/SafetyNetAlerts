package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de tests pour le contrôleur {@link FireStationController}.
 * Elle vérifie les comportements des endpoints liés aux casernes de pompiers
 * en simulant les appels HTTP et en validant les réponses attendues.
 */
@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    /**
     * Teste l'ajout d'une nouvelle caserne de pompiers.
     * L'endpoint doit retourner un message de succès si une caserne valide est ajoutée.
     *
     * @throws Exception En cas d'erreur lors de la requête.
     */
    @Test
    void ajouterCaserne_ShouldReturnSuccess() throws Exception {
        // Arrange: Préparation des données d'entrée et configuration du mock
        FireStation fireStation = new FireStation("123 Main St", "1");
        doNothing().when(fireStationService).addFireStation(any(FireStation.class));

        // Act & Assert: Exécution de la requête et vérification des résultats
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\", \"station\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne ajoutée avec succès."));
    }

    /**
     * Teste la mise à jour du numéro de station d'une caserne donnée.
     * L'endpoint doit retourner un message de succès si la mise à jour est effectuée.
     *
     * @throws Exception En cas d'erreur lors de la requête.
     */
    @Test
    void mettreAJourCaserne_ShouldReturnSuccess() throws Exception {
        // Arrange: Paramètres de la requête et configuration du mock
        String address = "123 Main St";
        String newStationNumber = "2";
        when(fireStationService.updateFireStation(address, newStationNumber)).thenReturn(true);

        // Act & Assert: Exécution de la requête et vérification des résultats
        mockMvc.perform(put("/firestation")
                        .param("address", address)
                        .param("newStationNumber", newStationNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne mise à jour avec succès."));
    }

    /**
     * Teste la suppression d'une caserne de pompiers.
     * L'endpoint doit retourner un message de succès si la caserne est supprimée.
     *
     * @throws Exception En cas d'erreur lors de la requête.
     */
    @Test
    void supprimerCaserne_ShouldReturnSuccess() throws Exception {
        // Arrange: Configuration des données de test et du mock
        String address = "123 Main St";
        Mockito.when(fireStationService.deleteFireStation(address)).thenReturn(true);

        // Act & Assert: Exécution de la requête et vérification des résultats
        mockMvc.perform(delete("/firestation")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne supprimée avec succès."));
    }

    /**
     * Teste la récupération de toutes les casernes de pompiers.
     * L'endpoint doit retourner une liste de casernes au format JSON.
     *
     * @throws Exception En cas d'erreur lors de la requête.
     */
    @Test
    void obtenirToutesLesCasernes_ShouldReturnList() throws Exception {
        // Arrange: Préparation des données retournées par le service
        FireStation fireStation1 = new FireStation("123 Main St", "1");
        FireStation fireStation2 = new FireStation("456 Maple Dr", "2");
        when(fireStationService.getAllFireStations()).thenReturn(List.of(fireStation1, fireStation2));

        // Act & Assert: Exécution de la requête et validation du JSON reçu
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[1].address").value("456 Maple Dr"))
                .andExpect(jsonPath("$[1].station").value("2"));
    }
}
