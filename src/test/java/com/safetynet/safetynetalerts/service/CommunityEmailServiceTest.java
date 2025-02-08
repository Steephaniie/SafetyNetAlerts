package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class CommunityEmailServiceTest {

    @MockBean
    private PersonService personService;

    @Autowired
    private CommunityEmailService communityEmailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation de Mockito
    }

    @Test
    void testGetEmailsByCity_WithValidCity_ReturnsEmails() {
        // Arrange
        String city = "Paris";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "la rue","Paris","34000","0102030405", "john.doe@email.com"),
                new Person("Jane", "Smith", "la rue","Paris","34000","0102030405", "jane.smith@email.com"),
                new Person("Mike", "Johnson","la rue", "Lyon","34000","0102030405", "mike.johnson@email.com") // Autre ville
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(2, result.getEmails().size());
        assertTrue(result.getEmails().contains("john.doe@email.com"));
        assertTrue(result.getEmails().contains("jane.smith@email.com"));
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetEmailsByCity_WithNoMatchingCity_ReturnsEmptyList() {
        // Arrange
        String city = "Marseille";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe","la rue", "Paris","34000","0102030405", "john.doe@email.com"),
                new Person("Jane", "Smith","la rue", "Lyon","34000","0102030405", "jane.smith@email.com")
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertTrue(result.getEmails().isEmpty());
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetEmailsByCity_WithNullEmails_IgnoresNullOrEmptyEmails() {
        // Arrange
        String city = "Paris";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "la rue","Paris","34000","0102030405", null), // Email nul
                new Person("Jane", "Smith","la rue", "Paris","34000","0102030405", ""), // Email vide
                new Person("Mike", "Brown", "la rue","Paris","34000","0102030405", "mike.brown@email.com") // Email valide
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(1, result.getEmails().size());
        assertTrue(result.getEmails().contains("mike.brown@email.com"));
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testGetEmailsByCity_WithEmptyPersonList_ReturnsEmptyList() {
        // Arrange
        String city = "Paris";
        when(personService.getAllPersons()).thenReturn(Collections.emptyList());

        // Act
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertTrue(result.getEmails().isEmpty());
        verify(personService, times(1)).getAllPersons();
    }
}