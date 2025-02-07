package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.MockBean;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MedicalRecordServiceTest {

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        // No setup needed as Spring Boot manages dependencies.
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    void testAddMedicalRecord() throws ParseException {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", dateFormat.parse("01/01/2000"), Arrays.asList("Aspirin"), Arrays.asList("Peanut allergy"));

        // Act
        medicalRecordService.addMedicalRecord(medicalRecord);

        // Assert
        verify(medicalRecordRepository, times(1)).addMedicalRecord(medicalRecord);
    }

    @Test
    void testUpdateMedicalRecord_Success() throws ParseException {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord updatedMedicalRecord = new MedicalRecord(firstName, lastName, dateFormat.parse("02/02/2001"), Arrays.asList("Ibuprofen"), Arrays.asList("None"));
        when(medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord)).thenReturn(true);

        // Act
        boolean result = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);

        // Assert
        assertTrue(result);
        verify(medicalRecordRepository, times(1)).updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
    }

    @Test
    void testUpdateMedicalRecord_Failure() throws ParseException {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord updatedMedicalRecord = new MedicalRecord(firstName, lastName, dateFormat.parse("02/02/2001"), Arrays.asList("Ibuprofen"), Arrays.asList("None"));
        when(medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord)).thenReturn(false);

        // Act
        boolean result = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);

        // Assert
        assertFalse(result);
        verify(medicalRecordRepository, times(1)).updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
    }

    @Test
    void testDeleteMedicalRecord_Success() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(medicalRecordRepository.deleteMedicalRecord(firstName, lastName)).thenReturn(true);

        // Act
        boolean result = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        // Assert
        assertTrue(result);
        verify(medicalRecordRepository, times(1)).deleteMedicalRecord(firstName, lastName);
    }

    @Test
    void testDeleteMedicalRecord_Failure() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        when(medicalRecordRepository.deleteMedicalRecord(firstName, lastName)).thenReturn(false);

        // Act
        boolean result = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        // Assert
        assertFalse(result);
        verify(medicalRecordRepository, times(1)).deleteMedicalRecord(firstName, lastName);
    }

    @Test
    void testGetAllMedicalRecords() throws ParseException {
        // Arrange
        List<MedicalRecord> medicalRecords = Arrays.asList(
                new MedicalRecord("John", "Doe", dateFormat.parse("01/01/2000"), Arrays.asList("Aspirin"), Arrays.asList("Peanut allergy")),
                new MedicalRecord("Jane", "Smith", dateFormat.parse("03/04/1990"), Arrays.asList("Tylenol"), Arrays.asList("None"))
        );
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecords);

        // Act
        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(medicalRecordRepository, times(1)).getMedicalRecords();
    }
}