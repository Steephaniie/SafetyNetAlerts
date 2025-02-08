package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.service.FireAlertService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireAlertController.class) // Charge uniquement le contexte du contrôleur
class FireAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireAlertService fireAlertService;

    @Test
    void testGetFireAlert_Returns200_WithValidAddress() throws Exception {
        // Given: une adresse valide avec un FireAlertDTO rempli
        String address = "123 Main St";
        FireAlertDTO fireAlertDTO = new FireAlertDTO();
        fireAlertDTO.setFireStationNumber("1");
        // Création d'un objet ResidentInfo
        FireAlertDTO.ResidentInfo residentInfo = new FireAlertDTO.ResidentInfo();
        residentInfo.setFirstName("John");
        residentInfo.setLastName("Doe");
        fireAlertDTO.setResidents(Collections.singletonList(residentInfo));
        when(fireAlertService.getFireAlertByAddress(address)).thenReturn(fireAlertDTO);

        // When & Then: appel de l'API et vérification de la réponse 200
        mockMvc.perform(get("/fire")
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fireStationNumber").value(1))
                .andExpect(jsonPath("$.residents[0].firstName").value("John"))
                .andExpect(jsonPath("$.residents[0].lastName").value("Doe"));
    }

    @Test
    void testGetFireAlert_Returns404_WhenAddressNotFound() throws Exception {
        // Given: une adresse pour laquelle FireAlertDTO est vide
        String address = "unknown address";
        FireAlertDTO fireAlertDTO = new FireAlertDTO(); // DTO vide
        fireAlertDTO.setResidents(Collections.emptyList());
        fireAlertDTO.setFireStationNumber(null);

        when(fireAlertService.getFireAlertByAddress(address)).thenReturn(fireAlertDTO);

        // When & Then: appel de l'API et vérification de la réponse 404
        mockMvc.perform(get("/fire")
                .param("address", address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}