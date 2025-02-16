package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.service.CommunityEmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de la classe CommunityEmailController permettant de vérifier
 * les points d'API liés au service CommunityEmail.
 */
@WebMvcTest(CommunityEmailController.class)
public class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityEmailService communityEmailService;

    /**
     * Vérifie que l'API retourne un statut HTTP 200 (OK) et le contenu JSON attendu
     * lorsqu'une ville valide est fournie.
     *
     * @throws Exception si une erreur survient lors de l'exécution du test
     */
    @Test
    void testGetEmailsByCity_ReturnsOk() throws Exception {
        // Préparation des données d'entrée et du mock
        String city = "Paris";
        CommunityEmailDTO communityEmailDTO = new CommunityEmailDTO(city,
                List.of("email1@example.com", "email2@example.com"));
        when(communityEmailService.getEmailsByCity(city)).thenReturn(communityEmailDTO);

        // Exécution et vérifications
        mockMvc.perform(get("/communityEmail")
                        .param("city", city) // Ajouter le paramètre HTTP "city"
                        .accept(MediaType.APPLICATION_JSON)) // Demande le type de contenu JSON
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 (OK)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Vérifie que le type de contenu est JSON
                .andExpect(jsonPath("$.city").value(city)) // Vérifie que la réponse contient la ville attendue
                .andExpect(jsonPath("$.emails[0]").value("email1@example.com")) // Vérifie que le premier email correspond
                .andExpect(jsonPath("$.emails[1]").value("email2@example.com")); // Vérifie que le second email correspond
    }

    /**
     * Vérifie que l'API retourne un statut HTTP 400 (Bad Request)
     * lorsque le paramètre 'city' est manquant dans la requête.
     *
     * @throws Exception si une erreur survient lors de l'exécution du test
     */
    @Test
    void testGetEmailsByCity_ReturnsBadRequest_WhenCityIsMissing() throws Exception {
        // Exécution et vérifications
        mockMvc.perform(get("/communityEmail")
                        .accept(MediaType.APPLICATION_JSON)) // Demande un contenu au format JSON
                .andExpect(status().isBadRequest()) // Vérifie que le statut HTTP est 400 (Bad Request)
                .andExpect(content().string("")); // Vérifie que la réponse est vide
    }

    /**
     * Vérifie que l'API retourne un statut HTTP 200 (OK) et une liste d'emails vide
     * lorsqu'aucun email n'est trouvé pour une ville donnée.
     *
     * @throws Exception si une erreur survient lors de l'exécution du test
     */
    @Test
    void testGetEmailsByCity_ReturnsEmptyList_WhenNoEmailsFound() throws Exception {
        // Préparation des données d'entrée et du mock
        String city = "UnknownCity";
        CommunityEmailDTO communityEmailDTO = new CommunityEmailDTO(city, List.of());
        when(communityEmailService.getEmailsByCity(city)).thenReturn(communityEmailDTO);

        // Exécution et vérifications
        mockMvc.perform(get("/communityEmail")
                        .param("city", city) // Ajouter le paramètre HTTP "city"
                        .accept(MediaType.APPLICATION_JSON)) // Demander un contenu JSON
                .andExpect(status().isOk()) // Vérifie que le statut HTTP est 200 (OK)
                .andExpect(jsonPath("$.city").value(city)) // Vérifie que la ville correspond
                .andExpect(jsonPath("$.emails").isEmpty()); // Vérifie que la liste des emails est vide
    }
}