package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class MedicalRecordRepositoryTest {

    @MockBean
    private JsonFileWriter jsonFileWriter;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialise une liste de départ pour les tests
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe",new Date(2000,1,1), List.of("aspirin"), List.of("pollen"));
        medicalRecords = new ArrayList<>();
        medicalRecords.add(medicalRecord);

        when(jsonFileWriter.getMedicalrecords()).thenReturn(medicalRecords);
    }

    /**
     * Teste l'ajout d'un nouveau dossier médical.
     * <p>
     * Cette méthode vérifie que le nouveau dossier médical est correctement ajouté
     * à la liste existante et que cela est bien enregistré via le writer JSON.
     */
    @Test
    void testAddMedicalRecord() {
        // Initialisation
        MedicalRecord newMedicalRecord = new MedicalRecord("Jane", "Smith", new Date(1990,2,2), List.of("ibuprofen"), List.of("dust"));

        // Appel de la méthode
        medicalRecordRepository.addMedicalRecord(newMedicalRecord);

        // Vérification
        assertTrue(medicalRecords.contains(newMedicalRecord), "Le dossier médical devrait être ajouté à la liste.");
        verify(jsonFileWriter, times(1)).setMedicalrecords(medicalRecords);
    }

    /**
     * Teste la mise à jour d'un dossier médical existant.
     * <p>
     * Cas de succès où un dossier médical existant est mis à jour avec de nouvelles valeurs
     * et où l'opération est correctement enregistrée.
     */
    @Test
    void testUpdateMedicalRecord_Success() {
        // Initialisation
        MedicalRecord updatedRecord = new MedicalRecord("John", "Doe", new Date(2001,1,1), List.of("ibuprofen"), List.of());

        // Appel de la méthode
        boolean result = medicalRecordRepository.updateMedicalRecord("John", "Doe", updatedRecord);

        // Vérification
        assertTrue(result, "La mise à jour devrait réussir.");
        assertEquals(new Date(2001,1,1), medicalRecords.get(0).getBirthDate());
        verify(jsonFileWriter, times(1)).setMedicalrecords(medicalRecords);
    }

    /**
     * Teste la mise à jour d'un dossier médical qui n'existe pas.
     * <p>
     * Cas d'échec où la tentative de mise à jour échoue car le dossier recherché
     * n'est pas présent dans la liste.
     */
    @Test
    void testUpdateMedicalRecord_Failure() {
        // Initialisation
        MedicalRecord updatedRecord = new MedicalRecord("Jane", "Smith", new Date (2001,1,1), List.of("ibuprofen"), List.of());

        // Appel de la méthode
        boolean result = medicalRecordRepository.updateMedicalRecord("Jane", "Smith", updatedRecord);

        // Vérification
        assertFalse(result, "La mise à jour devrait échouer car l'entrée n'existe pas.");
        verify(jsonFileWriter, never()).setMedicalrecords(any());
    }

    /**
     * Teste la suppression réussie d'un dossier médical.
     * <p>
     * Vérifie qu'un dossier médical existant est correctement supprimé de la liste
     * et que la suppression est bien enregistrée.
     */
    @Test
    void testDeleteMedicalRecord_Success() {
        // Appel de la méthode
        boolean result = medicalRecordRepository.deleteMedicalRecord("John", "Doe");

        // Vérification
        assertTrue(result, "La suppression devrait réussir.");
        assertTrue(medicalRecords.isEmpty(), "La liste devrait être vide après la suppression.");
        verify(jsonFileWriter, times(1)).setMedicalrecords(medicalRecords);
    }

    /**
     * Teste l'échec de la suppression d'un dossier médical.
     * <p>
     * Vérifie qu'une tentative de suppression échoue lorsqu'aucun dossier médical
     * correspondant n'est trouvé dans la liste.
     */
    @Test
    void testDeleteMedicalRecord_Failure() {
        // Appel de la méthode
        boolean result = medicalRecordRepository.deleteMedicalRecord("Jane", "Smith");

        // Vérification
        assertFalse(result, "La suppression devrait échouer car l'entrée n'existe pas.");
        assertEquals(1, medicalRecords.size(), "La liste devrait toujours contenir un élément.");
    }

    /**
     * Teste la récupération de tous les dossiers médicaux.
     * <p>
     * Vérifie que la méthode renvoie la liste complète des dossiers médicaux
     * actuellement stockés.
     */
    @Test
    void testGetMedicalRecords() {
        // Appel de la méthode
        List<MedicalRecord> result = medicalRecordRepository.getMedicalRecords();

        // Vérification
        assertEquals(1, result.size(), "Il devrait y avoir un seul dossier médical dans la liste.");
        assertEquals("John", result.get(0).getFirstName(), "Le prénom devrait correspondre à 'John'.");
    }
}