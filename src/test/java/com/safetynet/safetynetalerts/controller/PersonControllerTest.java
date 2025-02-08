package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    };

    @Test
    void testGetAllPersons() {
        // Given
        List<Person> mockPersons = Arrays.asList(
                new Person("John", "Doe","la rue", "Paris","34000","0102030405", "john.doe@email.com"),
                new Person("Jane", "Doe","la rue", "Lyon","34000","0102030405", "jane.smith@email.com")

        );
        when(personService.getAllPersons()).thenReturn(mockPersons);

        // When
        ResponseEntity<List<Person>> response = personController.getAllPersons();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPersons, response.getBody());
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void testAddPerson() {
        // Given
        Person newPerson =  new Person("John", "Doe","la rue", "Paris","34000","0102030405", "john.doe@email.com");

        when(personService.addPerson(newPerson)).thenReturn(newPerson);

        // When
        ResponseEntity<Person> response = personController.addPerson(newPerson);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newPerson, response.getBody());
        verify(personService, times(1)).addPerson(newPerson);
    }

    @Test
    void testUpdatePerson() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Person updatedPerson = new Person(firstName, lastName,"la rue", "Paris","34000","0102030405", "john.doe@email.com");

        when(personService.updatePerson(firstName, lastName, updatedPerson)).thenReturn(updatedPerson);

        // When
        ResponseEntity<Person> response = personController.updatePerson(firstName, lastName, updatedPerson);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPerson, response.getBody());
        verify(personService, times(1)).updatePerson(firstName, lastName, updatedPerson);
    }

    @Test
    void testUpdatePerson_NotFound() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        Person updatedPerson = new Person(firstName, lastName,"la rue", "Paris","34000","0102030405", "john.doe@email.com");
        when(personService.updatePerson(firstName, lastName, updatedPerson)).thenReturn(null);

        // When
        ResponseEntity<Person> response = personController.updatePerson(firstName, lastName, updatedPerson);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, times(1)).updatePerson(firstName, lastName, updatedPerson);
    }

    @Test
    void testDeletePerson() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        when(personService.deletePerson(firstName, lastName)).thenReturn(true);

        // When
        ResponseEntity<String> response = personController.deletePerson(firstName, lastName);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Personne supprimée avec succès.", response.getBody());
        verify(personService, times(1)).deletePerson(firstName, lastName);
    }

    @Test
    void testDeletePerson_NotFound() {
        // Given
        String firstName = "John";
        String lastName = "Doe";
        when(personService.deletePerson(firstName, lastName)).thenReturn(false);

        // When
        ResponseEntity<String> response = personController.deletePerson(firstName, lastName);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Personne non trouvée.", response.getBody());
        verify(personService, times(1)).deletePerson(firstName, lastName);
    }
}