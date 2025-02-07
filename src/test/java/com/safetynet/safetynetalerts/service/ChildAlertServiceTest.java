package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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

//@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        // Mock des données pour les tests
        mockPersons = Arrays.asList(
            new Person("John", "Doe", "123 Main St", "City", "12345", "john.doe@example.com", "123-456-7890"),
            new Person("Jane", "Doe", "123 Main St", "City", "12345", "jane.doe@example.com", "123-456-7891")
        );

        mockMedicalRecords = Arrays.asList(
            new MedicalRecord("John", "Doe", Date.from(LocalDate.of(2015, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()), Arrays.asList("medication1"), Arrays.asList("allergy1")),
            new MedicalRecord("Jane", "Doe", Date.from(LocalDate.of(1985, 12, 25).atStartOfDay(ZoneId.systemDefault()).toInstant()), Collections.emptyList(), Collections.emptyList())
        );
    }

    @Test
    void testGetChildrenAtAddress_WithChildren() {
        // Mock des services
        when(personService.getAllPersons()).thenReturn(mockPersons);
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(mockMedicalRecords);

        // Appel de la méthode à tester
        ChildAlertDTO result = childAlertService.getChildrenAtAddress("123 Main St");

        // Vérification du résultat
        assertNotNull(result);
        assertEquals(1, result.getChildren().size());
        assertEquals("John", result.getChildren().get(0).getFirstName());
        assertEquals(10, result.getChildren().get(0).getAge());

        assertEquals(1, result.getOtherHouseholdMembers().size());
        assertEquals("Jane", result.getOtherHouseholdMembers().get(0).getFirstName());
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    @Test
    void testGetChildrenAtAddress_NoChildren() {
        // Mock des services avec des âges adultes
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

    @Test
    void testGetChildrenAtAddress_InvalidAddress() {
        // Lorsque l'adresse n'est pas trouvée
        when(personService.getAllPersons()).thenReturn(Collections.emptyList());

        // Appel de la méthode à tester
        ChildAlertDTO result = childAlertService.getChildrenAtAddress("Invalid Address");

        // Vérification du résultat
        assertNotNull(result);
        assertTrue(result.getChildren().isEmpty());
        assertTrue(result.getOtherHouseholdMembers().isEmpty());
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, never()).getAllMedicalRecords();
    }

    @Test
    void testCalculateAge() {
        // Création d'une date de naissance
        Date birthDate = Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        int age = childAlertService.calculateAge(birthDate);

        // Vérification du résultat
        int expectedAge = LocalDate.now().getYear() - 2000;
        assertEquals(expectedAge, age);
    }
}
