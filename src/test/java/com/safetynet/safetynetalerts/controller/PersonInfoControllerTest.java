package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.service.PersonInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Classe de test pour le contrôleur {@link PersonInfoController}.
 * Elle vérifie les différents cas possibles liés à l'obtention
 * des informations de personnes via les endpoints REST.
 */
@WebMvcTest(PersonInfoController.class)
class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonInfoService personInfoService;

    // Exemple de données fictives pour les tests
    private List<PersonInfoDTO> personInfoDTOList;

    /**
     * Prépare les données fictives nécessaires aux tests.
     */
    @BeforeEach
    void setUp() {
        PersonInfoDTO person1 = new PersonInfoDTO(
                "John",
                "Doe",
                "123 Main St",
                35,
                "johndoe@email.com",
                List.of("Paracetamol:500mg", "Ibuprofen:200mg"),
                List.of("None")
        );
        PersonInfoDTO person2 = new PersonInfoDTO(
                "Jane",
                "Doe",
                "123 Main St",
                32,
                "janedoe@email.com",
                List.of("Aspirin:100mg"),
                List.of("Peanuts")
        );
        personInfoDTOList = Arrays.asList(person1, person2);
    }


    /**
     * Teste le cas où la requête GET renvoie avec succès
     * une liste d'informations pour un nom de famille donné.
     *
     * @throws Exception si une erreur survient lors de l'exécution de la requête.
     */
    @Test
    void testGetPersonInfo_Success() throws Exception {
        // Simule le service pour retourner une liste fictive de données.
        String lastName = "Doe";
        when(personInfoService.getPersonInfoByLastName(lastName)).thenReturn(personInfoDTOList);

        // Exécute une requête GET et vérifie si elle renvoie un statut 200 (OK)
        // Exécute une requête GET et vérifie que le statut est 200 (OK)
        MvcResult result = mockMvc.perform(get("/personInfo")
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Vérifie que le service est invoqué avec le bon paramètre "lastName"
        Mockito.verify(personInfoService).getPersonInfoByLastName(lastName);

        // Vérifie que la réponse JSON contient les données correspondantes à ce qui est attendu
        String expectedResponse = "[\n" +
                "    {\n" +
                "        \"firstName\": \"John\",\n" +
                "        \"lastName\": \"Doe\",\n" +
                "        \"address\": \"123 Main St\",\n" +
                "        \"age\": 35,\n" +
                "        \"email\": \"johndoe@email.com\",\n" +
                "        \"medications\": [\"Paracetamol:500mg\", \"Ibuprofen:200mg\"],\n" +
                "        \"allergies\": [\"None\"]\n" +
                "    },\n" +
                "    {\n" +
                "        \"firstName\": \"Jane\",\n" +
                "        \"lastName\": \"Doe\",\n" +
                "        \"address\": \"123 Main St\",\n" +
                "        \"age\": 32,\n" +
                "        \"email\": \"janedoe@email.com\",\n" +
                "        \"medications\": [\"Aspirin:100mg\"],\n" +
                "        \"allergies\": [\"Peanuts\"]\n" +
                "    }\n" +
                "]";
        ObjectMapper objectMapper = new ObjectMapper();
        String actualResponse = result.getResponse().getContentAsString();

// Convertit l'objet attendu en JSON pour comparaison
        String expectedResponseJson = objectMapper.writeValueAsString(personInfoDTOList);

// Comparaison des arbres JSON pour ignorer les différences de format
        assert objectMapper.readTree(actualResponse).equals(objectMapper.readTree(expectedResponseJson));
    }

    /**
     * Teste le cas où la requête GET pour un nom de famille donné
     * ne renvoie aucun résultat (liste vide).
     *
     * @throws Exception si une erreur survient lors de l'exécution de la requête.
     */
    @Test
    void testGetPersonInfo_EmptyResult() throws Exception {
        // Simule le service pour retourner une liste vide (aucun résultat trouvé).
        String lastName = "Smith";
        when(personInfoService.getPersonInfoByLastName(lastName)).thenReturn(List.of());

        // Exécute une requête GET et vérifie les résultats
        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Vérifie que le statut est 200
    }

    /**
     * Teste le cas où la requête GET échoue avec un statut 400 (Bad Request)
     * car le paramètre obligatoire "lastName" est manquant.
     *
     * @throws Exception si une erreur survient lors de l'exécution de la requête.
     */
    @Test
    void testGetPersonInfo_BadRequest_MissingLastName() throws Exception {
        // Envoie une requête GET sans inclure le paramètre obligatoire "lastName".
        // Envoie une requête au contrôleur sans le paramètre requis "lastName".
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Vérifie que le statut est 400
    }
}