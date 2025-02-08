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

@WebMvcTest(PersonInfoController.class)
class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonInfoService personInfoService;

    // Exemple de données fictives pour les tests
    private List<PersonInfoDTO> personInfoDTOList;

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


    @Test
    void testGetPersonInfo_Success() throws Exception {
        // Mock du service pour retourner la liste fictive
        String lastName = "Doe";
        when(personInfoService.getPersonInfoByLastName(lastName)).thenReturn(personInfoDTOList);

        // Exécute une requête GET et vérifie les résultats
        MvcResult result = mockMvc.perform(get("/personInfo")
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Vérifie que le service a été appelé avec le bon paramètre
        Mockito.verify(personInfoService).getPersonInfoByLastName(lastName);
        
        // Vérifie si la réponse contient les données JSON attendues
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

// Convertir l'objet attendu en JSON normalisé
        String expectedResponseJson = objectMapper.writeValueAsString(personInfoDTOList);

// Comparaison des arbres JSON pour ignorer les différences de format
        assert objectMapper.readTree(actualResponse).equals(objectMapper.readTree(expectedResponseJson));
    }

    @Test
    void testGetPersonInfo_EmptyResult() throws Exception {
        // Mock du service pour retourner une liste vide
        String lastName = "Smith";
        when(personInfoService.getPersonInfoByLastName(lastName)).thenReturn(List.of());

        // Exécute une requête GET et vérifie les résultats
        mockMvc.perform(get("/personInfo")
                .param("lastName", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Vérifie que le statut est 200
    }

    @Test
    void testGetPersonInfo_BadRequest_MissingLastName() throws Exception {
        // Requête sans paramètre "lastName"
        mockMvc.perform(get("/personInfo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // Vérifie que le statut est 400
    }
}