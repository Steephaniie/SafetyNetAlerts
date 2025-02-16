package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Classe de test pour le service {@link ChildAlertService}.
 * Elle vérifie les fonctionnalités en rapport avec la recherche des enfants à une adresse donnée,
 * ainsi que le calcul de leur âge.
 */
@SpringBootTest
public class ChildAlertServiceTest {

    @MockBean
    private PersonService personService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ChildAlertService childAlertService;

    private List<Person> mockPersons;
    private List<MedicalRecord> mockMedicalRecords;

    /**
     * Configuration des données de test avant l'exécution de chaque test.
     * Initialise des listes mockées de personnes et de dossiers médicaux.
     */
    @BeforeEach
    void setUp() {
        // Initialisation des données mockées pour représenter des personnes et leurs dossiers médicaux
        mockPersons = Arrays.asList(
            new Person("John", "Doe", "123 Main St", "City", "12345", "john.doe@example.com", "123-456-7890"),
            new Person("Jane", "Doe", "123 Main St", "City", "12345", "jane.doe@example.com", "123-456-7891")
        );

        mockMedicalRecords = Arrays.asList(
            new MedicalRecord("John", "Doe", Date.from(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), List.of("medication1"), List.of("allergy1")),
            new MedicalRecord("Jane", "Doe", Date.from(LocalDate.of(1985, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), Collections.emptyList(), Collections.emptyList())
        );
    }

    /**
     * Teste la méthode {@link ChildAlertService#getChildrenAtAddress(String)}
     * lorsqu'une adresse contient des enfants.
     */
    @Test
    void testGetChildrenAtAddress_WithChildren() {
        // Simulation des services pour fournir des données mockées
        when(personService.getAllPersons()).thenReturn(mockPersons);
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(mockMedicalRecords);

        // Appel de la méthode à tester
        ChildAlertDTO result = childAlertService.getChildrenAtAddress("123 Main St");

        // Vérification du résultat
        assertNotNull(result);
        assertEquals(1, result.getChildren().size());
        assertEquals("John", result.getChildren().get(0).getFirstName());
        assertEquals(10, result.getChildren().get(0).getAge());

        assertEquals(2, result.getOtherHouseholdMembers().size());
        assertEquals("Jane", result.getOtherHouseholdMembers().get(0).getFirstName());
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    /**
     * Teste la méthode {@link ChildAlertService#getChildrenAtAddress(String)}
     * lorsqu'une adresse ne contient aucun enfant (seulement des adultes).
     */
    @Test
    void testGetChildrenAtAddress_NoChildren() {
        // Simulation des services pour retourner des personnes uniquement adultes
        mockMedicalRecords = Arrays.asList(
                new MedicalRecord("John", "Doe", Date.from(LocalDate.of(1980, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), Collections.emptyList(), Collections.emptyList()),
                new MedicalRecord("Jane", "Doe", Date.from(LocalDate.of(1975, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), Collections.emptyList(), Collections.emptyList())
        );

        when(personService.getAllPersons()).thenReturn(mockPersons);
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(mockMedicalRecords);

        // Appel de la méthode à tester
        ChildAlertDTO result = childAlertService.getChildrenAtAddress("123 Main St");

        // Vérification du résultat
        assertNotNull(result);
        assertTrue(result.getChildren().isEmpty());
        assertTrue(result.getOtherHouseholdMembers().isEmpty());
    }

    /**
     * Teste la méthode {@link ChildAlertService#getChildrenAtAddress(String)}
     * lorsqu'une adresse invalide (non trouvée) est spécifiée.
     */
    @Test
    void testGetChildrenAtAddress_InvalidAddress() {
        // Simulation d'une adresse invalide avec une liste vide de personnes
        when(personService.getAllPersons()).thenReturn(Collections.emptyList());

        // Appel de la méthode à tester
        ChildAlertDTO result = childAlertService.getChildrenAtAddress("Invalid Address");

        // Vérification du résultat
        assertNotNull(result);
        assertTrue(result.getChildren().isEmpty());
        assertTrue(result.getOtherHouseholdMembers().isEmpty());
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    /**
     * Teste la méthode {@link ChildAlertService#calculateAge(Date)} pour
     * vérifier que l'âge est correctement calculé à partir d'une date de naissance donnée.
     */
    @Test
    void testCalculateAge() {
        // Création d'une date de naissance fictive pour le test
        Date birthDate = Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = childAlertService.calculateAge(birthDate);

        // Vérification du résultat
        int expectedAge = LocalDate.now().getYear() - 2000;
        assertEquals(expectedAge, age);
    }
}
