package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.service.FireAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour le contrôleur FireAlertController.
 * Cette classe utilise MockMvc pour tester les points d'entrée REST associés aux alertes d'incendie.
 */
@WebMvcTest(FireAlertController.class) // Charge uniquement le contexte du contrôleur
class FireAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireAlertService fireAlertService;

    /**
     * Teste que l'API retourne un statut HTTP 200 avec une réponse correcte
     * lorsqu'une adresse valide contenant des informations sur une station de feu et des résidents est fournie.
     *
     * @throws Exception en cas d'échec de l'exécution de MockMvc.
     */
    @Test
    void testGetFireAlert_Returns200_WithValidAddress() throws Exception {
        // Given: scénario avec une adresse valide et un DTO contenant des informations
        String address = "123 Main St";
        FireAlertDTO fireAlertDTO = new FireAlertDTO();
        fireAlertDTO.setFireStationNumber("1");
        // Création d'un objet ResidentInfo
        FireAlertDTO.ResidentInfo residentInfo = new FireAlertDTO.ResidentInfo();
        residentInfo.setFirstName("John");
        residentInfo.setLastName("Doe");
        fireAlertDTO.setResidents(Collections.singletonList(residentInfo));
        when(fireAlertService.getFireAlertByAddress(address)).thenReturn(fireAlertDTO);

        // When & Then: effectuer une requête GET sur l'API et vérifier que le statut est 200
        // ainsi que les données JSON récupérées correspondent aux valeurs attendues
        mockMvc.perform(get("/fire")
                        .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fireStationNumber").value(1))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"));
    }

    /**
     * Teste que l'API retourne un statut HTTP 404 lorsque l'adresse fournie
     * ne correspond à aucune alerte ou station de feu.
     *
     * @throws Exception en cas d'échec de l'exécution de MockMvc.
     */
    @Test
    void testGetFireAlert_Returns404_WhenAddressNotFound() throws Exception {
        // Given: une adresse inexistante qui retourne un DTO vide
        String address = "unknown address";
        FireAlertDTO fireAlertDTO = new FireAlertDTO(); // DTO vide
        fireAlertDTO.setResidents(Collections.emptyList());
        fireAlertDTO.setFireStationNumber(null);

        when(fireAlertService.getFireAlertByAddress(address)).thenReturn(fireAlertDTO);

        // When & Then: effectuer une requête GET sur l'API avec l'adresse inexistante
        // et vérifier que le statut HTTP renvoyé est bien 404
        mockMvc.perform(get("/fire")
                        .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}