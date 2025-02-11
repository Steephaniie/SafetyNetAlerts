package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Classe de test pour le contrôleur des dossiers médicaux, {@link MedicalRecordController}.
 * Teste les différentes fonctionnalités CRUD via des appels HTTP simulés avec {@link MockMvc}.
 */
// Annotation Spring Boot pour tester exclusivement le contrôleur
@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @BeforeEach
    void setUp() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Applique le fuseau UTC
    }

    /**
     * Teste l'ajout d'un dossier médical valide via une requête POST.
     * Vérifie que le statut HTTP retourné est 201 (CREATED) avec un message de succès et que le service est appelé une fois.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void addMedicalRecord_ShouldReturn201WhenValid() throws Exception {
        // Arrange : Configuration du mock pour ignorer l'ajout du dossier médical
        Mockito.doNothing().when(medicalRecordService).addMedicalRecord(any(MedicalRecord.class));

        // Données JSON simulées pour un dossier médical valide
        String medicalRecordJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthdate": "1990/01/01",
                    "medications": ["med1", "med2"],
                    "allergies": ["allergy1"]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicalRecordJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Dossier médical ajouté avec succès."));

        Mockito.verify(medicalRecordService, Mockito.times(1)).addMedicalRecord(any(MedicalRecord.class));
    }

    /**
     * Teste l'ajout d'un dossier médical invalide via une requête POST.
     * Vérifie que le statut HTTP retourné est 400 (BAD REQUEST) et que le service n'est pas appelé.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void addMedicalRecord_ShouldReturn400WhenInvalid() throws Exception {
        // Arrange : Données JSON simulées pour un dossier médical invalide
        String invalidMedicalRecordJson = """
                {
                    "firstName": "",
                    "lastName": "",
                    "birthDate": null,
                    "medications": [],
                    "allergies": []
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidMedicalRecordJson))
                .andExpect(status().isBadRequest());

        Mockito.verify(medicalRecordService, Mockito.never()).addMedicalRecord(any(MedicalRecord.class));
    }

    /**
     * Teste la mise à jour d'un dossier médical existant via une requête PUT.
     * Vérifie que le statut HTTP retourné est 200 (OK) et que le service est appelé une fois avec des données valides.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void updateMedicalRecord_ShouldReturn200WhenValid() throws Exception {
        // Arrange : Configuration du mock pour retourner un succès de mise à jour
        Mockito.when(medicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(true);

        // Données JSON simulées pour la mise à jour du dossier médical
        String updatedMedicalRecordJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthdate": "1989/12/31",
                    "medications": ["med3"],
                    "allergies": ["allergy2"]
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/medicalRecord/John/Doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMedicalRecordJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical mis à jour avec succès."));

        Mockito.verify(medicalRecordService, Mockito.times(1)).updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class));
    }

    /**
     * Teste la mise à jour d'un dossier médical inexistant via une requête PUT.
     * Vérifie que le statut HTTP retourné est 404 (NOT FOUND) et que le service est appelé une fois.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void updateMedicalRecord_ShouldReturn404WhenNotFound() throws Exception {
        // Arrange : Configuration du mock pour retourner un échec de mise à jour
        Mockito.when(medicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(false);

        // Données JSON simulées pour la mise à jour du dossier médical inexistant
        String updatedMedicalRecordJson = """
                {
                    "firstName": "Jane",
                    "lastName": "Smith",
                    "birthdate": "1985/05/05",
                    "medications": ["med4"],
                    "allergies": ["allergy3"]
                }
                """;

        // Act & Assert
        mockMvc.perform(put("/medicalRecord/Jane/Smith")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMedicalRecordJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical non trouvé pour mise à jour."));

        Mockito.verify(medicalRecordService, Mockito.times(1)).updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class));
    }

    /**
     * Teste la suppression d'un dossier médical existant via une requête DELETE.
     * Vérifie que le statut HTTP retourné est 200 (OK) avec un message de succès et que le service est appelé une fois.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void deleteMedicalRecord_ShouldReturn200WhenSuccessful() throws Exception {
        // Arrange : Configuration du mock pour un succès de suppression
        Mockito.when(medicalRecordService.deleteMedicalRecord(anyString(), anyString())).thenReturn(true);

        // Act & Assert : Simulation de la demande de suppression et vérifications
        mockMvc.perform(delete("/medicalRecord/John/Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("Dossier médical supprimé avec succès."));

        Mockito.verify(medicalRecordService, Mockito.times(1)).deleteMedicalRecord(anyString(), anyString());
    }

    @Test
    void deleteMedicalRecord_ShouldReturn404WhenNotFound() throws Exception {
        // Arrange
        Mockito.when(medicalRecordService.deleteMedicalRecord(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/medicalRecord/Jane/Smith"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical non trouvé pour suppression."));

        Mockito.verify(medicalRecordService, Mockito.times(1)).deleteMedicalRecord(anyString(), anyString());
    }

    /**
     * Teste la récupération de tous les dossiers médicaux lorsque des dossiers existent via une requête GET.
     * Vérifie que le statut HTTP retourné est 200 (OK) et que les données JSON retournées correspondent aux attentes.
     *
     * @throws Exception en cas de problème avec la requête simulée.
     */
    @Test
    void getAllMedicalRecords_ShouldReturnListWhenRecordsExist() throws Exception {
        // Arrange : Création de données de test et configuration du mock pour retourner ces données
        MedicalRecord record = new MedicalRecord("John", "Doe", dateFormat.parse("31/12/1989"), List.of("allergy1"), List.of("med1"));
        System.out.println("Date enregistrée dans le test : " + dateFormat.format(record.getBirthDate()));
        Mockito.when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(record));

        // Act & Assert : Simulation de la demande et vérifications sur les données retournées
        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthdate").value("31/12/1989"))
                .andExpect(jsonPath("$[0].allergies[0]").value("allergy1"))
                .andExpect(jsonPath("$[0].medications[0]").value("med1"));

        Mockito.verify(medicalRecordService, Mockito.times(1)).getAllMedicalRecords();
    }

    @Test
    void getAllMedicalRecords_ShouldReturnEmptyListWhenNoRecords() throws Exception {
        // Arrange
        Mockito.when(medicalRecordService.getAllMedicalRecords()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(medicalRecordService, Mockito.times(1)).getAllMedicalRecords();
    }
}
