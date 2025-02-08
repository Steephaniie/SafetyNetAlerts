package com.safetynet.safetynetalerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.ResultActions;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void addMedicalRecord_ShouldReturn201WhenValid() throws Exception {
        // Arrange
        Mockito.doNothing().when(medicalRecordService).addMedicalRecord(any(MedicalRecord.class));


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

    @Test
    void addMedicalRecord_ShouldReturn400WhenInvalid() throws Exception {
        // Arrange
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

    @Test
    void updateMedicalRecord_ShouldReturn200WhenValid() throws Exception {
        // Arrange
        Mockito.when(medicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(true);

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

    @Test
    void updateMedicalRecord_ShouldReturn404WhenNotFound() throws Exception {
        // Arrange
        Mockito.when(medicalRecordService.updateMedicalRecord(anyString(), anyString(), any(MedicalRecord.class)))
                .thenReturn(false);

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

    @Test
    void deleteMedicalRecord_ShouldReturn200WhenSuccessful() throws Exception {
        // Arrange
        Mockito.when(medicalRecordService.deleteMedicalRecord(anyString(), anyString())).thenReturn(true);

        // Act & Assert
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

    @Test
    void getAllMedicalRecords_ShouldReturnListWhenRecordsExist() throws Exception {
        // Arrange
        MedicalRecord record = new MedicalRecord("John", "Doe", dateFormat.parse("31/12/1989"), List.of("allergy1"), List.of("med1"));
        System.out.println("Date enregistrée dans le test : " + dateFormat.format(record.getBirthDate()));
        Mockito.when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(record));

        // Act & Assert
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
