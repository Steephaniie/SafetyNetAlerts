package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.service.CommunityEmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityEmailService communityEmailService;

    @Test
    void testGetEmailsByCity_ReturnsOk() throws Exception {
        // Arrange
        String city = "Paris";
        CommunityEmailDTO communityEmailDTO = new CommunityEmailDTO(city, 
            List.of("email1@example.com", "email2@example.com"));
        when(communityEmailService.getEmailsByCity(city)).thenReturn(communityEmailDTO);

        // Act & Assert
        mockMvc.perform(get("/communityEmail")
                .param("city", city) // paramètre HTTP
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Vérifie le code HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.city").value(city)) // Vérifie que la réponse contient la bonne ville
                .andExpect(jsonPath("$.emails[0]").value("email1@example.com"))
                .andExpect(jsonPath("$.emails[1]").value("email2@example.com"));
    }

    @Test
    void testGetEmailsByCity_ReturnsBadRequest_WhenCityIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/communityEmail")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // Vérifie le code HTTP 400
                .andExpect(content().string("")); // La réponse peut être vide pour cette erreur
    }

    @Test
    void testGetEmailsByCity_ReturnsEmptyList_WhenNoEmailsFound() throws Exception {
        // Arrange
        String city = "UnknownCity";
        CommunityEmailDTO communityEmailDTO = new CommunityEmailDTO(city, List.of());
        when(communityEmailService.getEmailsByCity(city)).thenReturn(communityEmailDTO);

        // Act & Assert
        mockMvc.perform(get("/communityEmail")
                .param("city", city)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value(city))
                .andExpect(jsonPath("$.emails").isEmpty()); // Liste vide
    }
}