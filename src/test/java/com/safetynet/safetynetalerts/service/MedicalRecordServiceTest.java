package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Classe de test pour le service MedicalRecordService.
 * Elle contient plusieurs cas de test pour valider les fonctionnalités du service,
 * notamment l'ajout, la mise à jour, la suppression et la récupération des dossiers médicaux.
 */
@SpringBootTest
class MedicalRecordServiceTest {

    @MockBean
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        // Aucune configuration nécessaire car Spring Boot gère les dépendances.
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Test de l'ajout d'un dossier médical.
     * Scénario : Un nouveau dossier médical est ajouté avec des données valides.
     * Vérifie que la méthode du service appelle bien la méthode correspondante dans le MedicalRecordRepository.
     */
    @Test
    void testAddMedicalRecord() throws ParseException {
        // Préparation des données
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", dateFormat.parse("01/01/2000"), List.of("Aspirin"), List.of("Peanut allergy"));

        // Exécution de l'action
        medicalRecordService.addMedicalRecord(medicalRecord);

        // Vérification
        verify(medicalRecordRepository, times(1)).addMedicalRecord(medicalRecord);
    }

    /**
     * Test de mise à jour d'un dossier médical avec succès.
     * Scénario : La mise à jour des informations d'un dossier médical existant est demandée.
     * Vérifie que la méthode du service appelle correctement le repository et retourne "true" pour indiquer le succès de l'opération.
     */
    @Test
    void testUpdateMedicalRecord_Success() throws ParseException {
        // Préparation des données
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord updatedMedicalRecord = new MedicalRecord(firstName, lastName, dateFormat.parse("02/02/2001"), List.of("Ibuprofen"), List.of("None"));
        when(medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord)).thenReturn(true);

        // Exécution de l'action
        boolean result = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);

        // Vérification
        assertTrue(result);
        verify(medicalRecordRepository, times(1)).updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
    }

    /**
     * Test de mise à jour d'un dossier médical échouée.
     * Scénario : Une tentative de mise à jour d'un dossier médical échoue car la personne n'existe pas.
     * Vérifie que la méthode retourne "false" et que la méthode du repository est appelée une fois.
     */
    @Test
    void testUpdateMedicalRecord_Failure() throws ParseException {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        MedicalRecord updatedMedicalRecord = new MedicalRecord(firstName, lastName, dateFormat.parse("02/02/2001"), List.of("Ibuprofen"), List.of("None"));
        when(medicalRecordRepository.updateMedicalRecord(firstName, lastName, updatedMedicalRecord)).thenReturn(false);

        // Act
        boolean result = medicalRecordService.updateMedicalRecord(firstName, lastName, updatedMedicalRecord);

        // Assert
        assertFalse(result);
        verify(medicalRecordRepository, times(1)).updateMedicalRecord(firstName, lastName, updatedMedicalRecord);
    }

    /**
     * Test de suppression d'un dossier médical avec succès.
     * Scénario : Une demande de suppression d'un dossier existant est réalisée correctement.
     * Vérifie que la méthode du service retourne "true" et que le repository est appelé exactement une fois.
     */
    @Test
    void testDeleteMedicalRecord_Success() {
        // Préparation des données
        String firstName = "John";
        String lastName = "Doe";
        when(medicalRecordRepository.deleteMedicalRecord(firstName, lastName)).thenReturn(true);

        // Exécution de l'action
        boolean result = medicalRecordService.deleteMedicalRecord(firstName, lastName);

        // Vérification
        assertTrue(result);
        verify(medicalRecordRepository, times(1)).deleteMedicalRecord(firstName, lastName);
    }

    /**
     * Test de suppression d'un dossier médical échouée.
     * Scénario : Une tentative de suppression échoue car le dossier médical n'est pas trouvé.
     * Vérifie que la méthode retourne "false" et que le repository est tenu de traiter la demande.
     */
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

    /**
     * Test de récupération de tous les dossiers médicaux.
     * Scénario : Récupérer l'ensemble des dossiers médicaux depuis la base de données.
     * Vérifie que la liste renvoyée par le service correspond bien à celle fournie par le repository.
     */
    @Test
    void testGetAllMedicalRecords() throws ParseException {
        // Préparation des données
        List<MedicalRecord> medicalRecords = Arrays.asList(
                new MedicalRecord("John", "Doe", dateFormat.parse("01/01/2000"), List.of("Aspirin"), List.of("Peanut allergy")),
                new MedicalRecord("Jane", "Smith", dateFormat.parse("03/04/1990"), List.of("Tylenol"), List.of("None"))
        );
        when(medicalRecordRepository.getMedicalRecords()).thenReturn(medicalRecords);

        // Exécution de l'action
        List<MedicalRecord> result = medicalRecordService.getAllMedicalRecords();

        // Vérification
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(medicalRecordRepository, times(1)).getMedicalRecords();
    }
}