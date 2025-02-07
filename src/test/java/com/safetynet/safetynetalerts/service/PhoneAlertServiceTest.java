package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class PhoneAlertServiceTest {

    @Autowired
    private PhoneAlertService phoneAlertService;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private PersonService personService;

    @Test
    void testGetPhonesByFireStation_withValidStationNumber() {
        // Arrange
        String stationNumber = "1";

        List<FireStation> fireStations = Arrays.asList(
                new FireStation("123 Main St", "1"),
                new FireStation("456 Elm St", "1"),
                new FireStation("789 Oak St", "2")
        );
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "12345", "111-111-1111","email1@sfr.fr"),
                new Person("Jane", "Doe", "456 Elm St", "City", "12345", "222-222-2222","email1@sfr.fr"),
                new Person("Jim", "Beam", "789 Oak St", "City", "12345", "333-333-3333","email1@sfr.fr")
        );

        when(fireStationService.getAllFireStations()).thenReturn(fireStations);
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        PhoneAlertDTO result = phoneAlertService.getPhonesByFireStation(stationNumber);

        // Assert
        List<String> expectedPhoneNumbers = Arrays.asList("111-111-1111", "222-222-2222");
        assertEquals(expectedPhoneNumbers, result.getPhoneNumbers());
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetPhonesByFireStation_withNoMatchingStationNumber() {
        // Arrange
        String stationNumber = "3";

        List<FireStation> fireStations = Arrays.asList(
                new FireStation("123 Main St", "1"),
                new FireStation("456 Elm St", "1"),
                new FireStation("789 Oak St", "2")
        );
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "12345", "111-111-1111","email1@sfr.fr"),
                new Person("Jane", "Doe", "456 Elm St", "City", "12345", "222-222-2222","email1@sfr.fr"),
                new Person("Jim", "Beam", "789 Oak St", "City", "12345", "333-333-3333","email1@sfr.fr")
        );

        when(fireStationService.getAllFireStations()).thenReturn(fireStations);
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        PhoneAlertDTO result = phoneAlertService.getPhonesByFireStation(stationNumber);

        // Assert
        assertEquals(Collections.emptyList(), result.getPhoneNumbers());
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetPhonesByFireStation_withNoAddressesFound() {
        // Arrange
        String stationNumber = "1";

        List<FireStation> fireStations = Collections.emptyList();
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "12345", "111-111-1111","email1@sfr.fr"),
                new Person("Jane", "Doe", "456 Elm St", "City", "12345", "222-222-2222","email1@sfr.fr")
        );

        given(fireStationService.getAllFireStations()).willReturn(fireStations);
        given(personService.getAllPersons()).willReturn(persons);

        // Act
        PhoneAlertDTO result = phoneAlertService.getPhonesByFireStation(stationNumber);

        // Assert
        assertEquals(Collections.emptyList(), result.getPhoneNumbers());
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetPhonesByFireStation_withDuplicatePhoneNumbers() {
        // Arrange
        String stationNumber = "1";

        List<FireStation> fireStations = Arrays.asList(
                new FireStation("123 Main St", "1"),
                new FireStation("456 Elm St", "1")
        );
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "123 Main St", "City", "12345", "111-111-1111","email1@sfr.fr"),
                new Person("Jane", "Doe", "123 Main St", "City", "12345", "111-111-1111","email1@sfr.fr"),
                new Person("Jim", "Beam", "456 Elm St", "City", "12345", "222-222-2222","email1@sfr.fr")
        );

        when(fireStationService.getAllFireStations()).thenReturn(fireStations);
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        PhoneAlertDTO result = phoneAlertService.getPhonesByFireStation(stationNumber);

        // Assert
        List<String> expectedPhoneNumbers = Arrays.asList("111-111-1111", "222-222-2222");
        assertEquals(expectedPhoneNumbers, result.getPhoneNumbers());
        verify(fireStationService, times(1)).getAllFireStations();
        verify(personService, times(1)).getAllPersons();
    }
}