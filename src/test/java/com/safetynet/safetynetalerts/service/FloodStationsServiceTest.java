package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FloodStationsDTO;
import com.safetynet.safetynetalerts.dto.FloodStationsDTO.HouseholdInfo;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.FireStation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest
class FloodStationsServiceTest {

    @MockBean
    private PersonService personService;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private FloodStationsService floodStationsService;

    @Test
    void testGetHouseholdsByStations() {
        // Mock des casernes
        List<FireStation> fireStations = Arrays.asList(
                new FireStation("123 Main St", "1"),
                new FireStation("456 Elm St", "2")
        );
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);

        // Mock des personnes
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "11111", "111-111-1111","email1@sfr.fr"),
                new Person("Jane", "Smith", "456 Elm St", "City", "11111", "222-222-2222","email2@sfr.fr"),
                new Person("Someone", "Else", "789 Oak St", "City", "11111", "333-333-3333","email2@sfr.fr") // Pas dans une adresse couverte
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Mock des dossiers médicaux
        List<MedicalRecord> medicalRecords = Arrays.asList(
                new MedicalRecord("John", "Doe", new Date(System.currentTimeMillis() - 1000000000L),
                        Arrays.asList("Allergy1", "Allergy2"), Arrays.asList("Med1", "Med2")),
                new MedicalRecord("Jane", "Smith", new Date(System.currentTimeMillis() - 2000000000L),
                        Collections.emptyList(),Arrays.asList("Med3"))
        );
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);

        // Appel de la méthode à tester
        List<String> stationNumbers = Arrays.asList("1", "2");
        FloodStationsDTO result = floodStationsService.getHouseholdsByStations(stationNumbers);

        // Vérifications
        assertNotNull(result);
        assertEquals(2, result.getHouseholdsByAddress().size());

        // Vérifions les données de "123 Main St"
        List<HouseholdInfo> household1 = result.getHouseholdsByAddress().get("123 Main St");
        assertNotNull(household1);
        assertEquals(1, household1.size());
        assertEquals("John", household1.get(0).getFirstName());
        assertEquals("Doe", household1.get(0).getLastName());
        assertEquals("111-111-1111", household1.get(0).getPhone());
        assertEquals(Arrays.asList("Med1", "Med2"), household1.get(0).getMedications());
        assertEquals(Arrays.asList("Allergy1", "Allergy2"), household1.get(0).getAllergies());

        // Vérifions les données de "456 Elm St"
        List<HouseholdInfo> household2 = result.getHouseholdsByAddress().get("456 Elm St");
        assertNotNull(household2);
        assertEquals(1, household2.size());
        assertEquals("Jane", household2.get(0).getFirstName());
        assertEquals("Smith", household2.get(0).getLastName());
        assertEquals("222-222-2222", household2.get(0).getPhone());
        assertEquals(Arrays.asList("Med3"), household2.get(0).getMedications());
        assertEquals(Collections.emptyList(), household2.get(0).getAllergies());

        // Vérification des mocks
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }

    @Test
    void testGetHouseholdsByStations_EmptyResult() {
        // Aucun mock des casernes
        when(fireStationService.getAllFireStations()).thenReturn(Collections.emptyList());

        // Appel de la méthode à tester
        List<String> stationNumbers = Collections.singletonList("1");
        FloodStationsDTO result = floodStationsService.getHouseholdsByStations(stationNumbers);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.getHouseholdsByAddress().isEmpty());

        // Vérification des mocks
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
        verify(medicalRecordService, times(1)).getAllMedicalRecords();
    }
}