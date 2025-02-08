package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Test
    void testAddPerson_Success() {
        // Arrange
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "0123456789", "email@example.com");
        when(personRepository.addPerson(person)).thenReturn(person);

        // Act
        Person result = personService.addPerson(person);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(personRepository, times(1)).addPerson(person);
    }

    @Test
    void testAddPerson_Failure_NullName() {
        // Arrange
        Person person = new Person(null, "Doe", "123 Main St", "City", "12345", "0123456789", "email@example.com");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> personService.addPerson(person));
        assertEquals("Le prénom et le nom sont obligatoires", exception.getMessage());
        verify(personRepository, never()).addPerson(any());
    }

    @Test
    void testUpdatePerson_Success() {
        // Arrange
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "City", "67890", "0987654321", "newemail@example.com");
        when(personRepository.updatePerson("John", "Doe", updatedPerson)).thenReturn(updatedPerson);

        // Act
        Person result = personService.updatePerson("John", "Doe", updatedPerson);

        // Assert
        assertNotNull(result);
        assertEquals("456 Elm St", result.getAddress());
        verify(personRepository, times(1)).updatePerson("John", "Doe", updatedPerson);
    }

    @Test
    void testUpdatePerson_PersonNotFound() {
        // Arrange
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "City", "67890", "0987654321", "newemail@example.com");
        when(personRepository.updatePerson("John", "Doe", updatedPerson)).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> personService.updatePerson("John", "Doe", updatedPerson));
        assertEquals("Personne non trouvée pour la mise à jour", exception.getMessage());
        verify(personRepository, times(1)).updatePerson("John", "Doe", updatedPerson);
    }

    @Test
    void testDeletePerson_Success() {
        // Arrange
        when(personRepository.deletePerson("John", "Doe")).thenReturn(true);

        // Act
        boolean result = personService.deletePerson("John", "Doe");

        // Assert
        assertTrue(result);
        verify(personRepository, times(1)).deletePerson("John", "Doe");
    }

    @Test
    void testDeletePerson_Failure_NotFound() {
        // Arrange
        when(personRepository.deletePerson("John", "Doe")).thenReturn(false);

        // Act & Assert
        assertFalse(personService.deletePerson("John", "Doe"));
        verify(personRepository, times(1)).deletePerson("John", "Doe");
    }

    @Test
    void testGetAllPersons_Success() {
        // Arrange
        Person person1 = new Person("John", "Doe", "123 Main St", "City", "12345", "0123456789", "email@example.com");
        Person person2 = new Person("Jane", "Doe", "456 Elm St", "City", "67890", "0987654321", "email2@example.com");
        List<Person> persons = Arrays.asList(person1, person2);
        when(personRepository.getAllPersons()).thenReturn(persons);

        // Act
        List<Person> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(personRepository, times(1)).getAllPersons();
    }

    @Test
    void testGetAllPersons_EmptyList() {
        // Arrange
        when(personRepository.getAllPersons()).thenReturn(List.of());

        // Act
        List<Person> result = personService.getAllPersons();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(personRepository, times(1)).getAllPersons();
    }
}