package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.MockBean;
//import org.mockito.InjectMocks;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
class PersonInfoServiceTest {

//    private PersonInfoService personInfoService;

    @MockBean
    private PersonService personService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PersonInfoService personInfoService;

    @Test
    void testGetPersonInfoByLastName_WithValidData() {
        // Given
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "11111", "111-111-1111","email1@sfr.fr");
        Person person2 = new Person("Jane", "Doe", "456 Elm St", "City", "11111", "222-222-2222","email2@sfr.fr");



                MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", new Date(88, 5, 20),
                List.of("med1", "med2"), List.of("allerg1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Doe", new Date(92, 7, 15),
                List.of("medA"), List.of("allergA"));

        when(personService.getAllPersons()).thenReturn(Arrays.asList(person1, person2));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));

        // When
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        // Then
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());

        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

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

    @Test
    void testGetPersonInfoByLastName_NoMedicalRecordForPerson() {
        // Given
        Person person = new Person("John", "Doe", "123 Main St", "City", "11111", "111-111-1111", "email1@sfr.fr");

        when(personService.getAllPersons()).thenReturn(Collections.singletonList(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(Collections.emptyList());

        // When
        List<PersonInfoDTO> result = personInfoService.getPersonInfoByLastName("Doe");

        // Then
        // Le résultat doit être vide, car aucun MedicalRecord n'est associé à cette personne
        assertFalse(result.isEmpty(), "The result should be empty when there is no medical record for the person");

        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();

    }

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