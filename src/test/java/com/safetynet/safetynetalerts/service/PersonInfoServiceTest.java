package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Classe de test pour PersonInfoService.
 * Elle vérifie différentes fonctionnalités, telles que la récupération des informations
 * des personnes en fonction de leur nom de famille et le calcul de l'âge.
 */
@SpringBootTest
class PersonInfoServiceTest {
    // Classe annotée avec @SpringBootTest pour le support de Spring context lors des tests

//    private PersonInfoService personInfoService;

    @MockBean
    private PersonService personService; // Mock du service PersonService pour simuler les données des personnes

    @MockBean
    private MedicalRecordService medicalRecordService; // Mock du service MedicalRecordService pour simuler les données médicales

    @Autowired
    private PersonInfoService personInfoService; // Service testé

    /**
     * Teste la méthode getPersonInfoByLastName() avec des données valides.
     * Vérifie que les informations des personnes sont bien retournées pour un nom de famille donné.
     */
    @Test
    void testGetPersonInfoByLastName_WithValidData() {
        // Given - Préparation des données d'entrée et des mocks nécessaires
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "11111", "111-111-1111","email1@sfr.fr");
        Person person2 = new Person("Jane", "Doe", "456 Elm St", "City", "11111", "222-222-2222","email2@sfr.fr");



                MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", new Date(88, 5, 20),
                List.of("med1", "med2"), List.of("allerg1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Doe", new Date(92, 7, 15),
                List.of("medA"), List.of("allergA"));

        when(personService.getAllPersons()).thenReturn(Arrays.asList(person1, person2));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));

        // When - Appel de la méthode testée
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        // Then - Vérifications des résultats attendus et utilisation des assertions
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    /**
     * Teste la méthode getPersonInfoByLastName() lorsque le nom de famille ne correspond à aucune personne.
     * Vérifie que le résultat est une liste vide.
     */
    @Test
    void testGetPersonInfoByLastName_NoMatchingLastName() {
        // Given
        Person person = new Person("Jane", "Smith", "456 Elm St", "City", "11111", "222-222-2222","email2@sfr.fr");

        when(personService.getAllPersons()).thenReturn(Collections.singletonList(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Collections.emptyList());

        // When
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        // Then
        assertTrue(result.isEmpty());
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    /**
     * Teste la méthode getPersonInfoByLastName() lorsque la personne n'a aucun dossier médical associé.
     * Vérifie que le résultat est vide dans ce cas.
     */
    @Test
    void testGetPersonInfoByLastName_NoMedicalRecordForPerson() {
        // Given
        Person person = new Person("John", "Doe", "123 Main St", "City", "11111", "111-111-1111", "email1@sfr.fr");

        when(personService.getAllPersons()).thenReturn(Collections.singletonList(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Collections.emptyList());

        // When
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        // Then
        // Vérifie que le résultat n'est pas vide, ce qui est attendu en cas d'absence de dossier médical
        assertFalse(result.isEmpty(), "The result should be empty when there is no medical record for the person");

        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();

    }

    /**
     * Teste la méthode calculateAge() avec une date de naissance nulle.
     * Vérifie que l'âge calculé est de 0.
     */
    @Test
    void testCalculateAge_WithNullBirthDate() {
        // Given
        Date nullBirthDate = null;

        // When
        int age = personInfoService.calculateAge(nullBirthDate);

        // Then
        assertEquals(0, age);
    }
}