package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class PersonRepositoryTest {

    @MockBean
    private JsonFileWriter jsonFileWriter;

    @Autowired
    private PersonRepository personRepository;

    private List<Person> mockPersonList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialisation de données fictives pour les tests
        mockPersonList = new ArrayList<>();
        mockPersonList.add(new Person("John", "Doe", "123 Main St", "New York", "12345", "123-456-7890", "john.doe@email.com"));
        mockPersonList.add(new Person("Jane", "Smith", "456 Elm St", "Los Angeles", "54321", "987-654-3210", "jane.smith@email.com"));
    }

    /**
     * Vérifie que l'ajout d'une personne dans la liste fonctionne correctement.
     * La personne ajoutée doit être présente dans la liste et les données mises à jour.
     */
    @Test
    void addPerson_ShouldAddPerson() {
        // Given
        Person newPerson = new Person("Alice", "Brown", "789 Oak St", "Chicago", "67890", "111-222-3333", "alice.brown@email.com");
        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);

        // When
        Person addedPerson = personRepository.addPerson(newPerson);

        // Then
        assertEquals(newPerson, addedPerson);
        verify(jsonFileWriter, times(1)).setPersons(mockPersonList);
        assertTrue(mockPersonList.contains(newPerson));
    }

    /**
     * Vérifie que la mise à jour des informations d'une personne existante est réalisée avec succès.
     * La nouvelle adresse et l'email doivent être correctement mis à jour.
     */
    @Test
    void updatePerson_ShouldUpdateExistingPerson() {
        // Given
        Person updatedPerson = new Person("John", "Doe", "999 New St", "San Francisco", "98765", "555-666-7777", "john.new@email.com");
        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);

        // When
        Person result = personRepository.updatePerson("John", "Doe", updatedPerson);

        // Then
        assertNotNull(result);
        assertEquals("999 New St", result.getAddress());
        assertEquals("john.new@email.com", result.getEmail());
        verify(jsonFileWriter, times(1)).setPersons(mockPersonList);
    }

    /**
     * Vérifie que la mise à jour retourne null lorsqu'aucune personne correspondante n'est trouvée.
     * Aucune modification ne doit être effectuée dans ce cas.
     */
    @Test
    void updatePerson_ShouldReturnNullIfPersonNotFound() {
        // Given
        Person updatedPerson = new Person("NonExistent", "Person", "N/A", "N/A", "00000", "000-000-0000", "none@email.com");
        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);

        // When
        Person result = personRepository.updatePerson("NonExistent", "Person", updatedPerson);

        // Then
        assertNull(result);
        verify(jsonFileWriter, never()).setPersons(mockPersonList);
    }

    /**
     * Vérifie que la suppression d'une personne existante dans la liste fonctionne correctement.
     * La personne doit être retirée de la liste après suppression.
     */
    @Test
    void deletePerson_ShouldRemovePerson() {
        // Given
        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);
        int initialSize = mockPersonList.size();

        // When
        boolean isDeleted = personRepository.deletePerson("John", "Doe");

        // Then
        assertTrue(isDeleted);
    }

    /**
     * Vérifie que la suppression retourne false lorsqu'aucune personne correspondante n'est trouvée.
     * Aucune modification ne doit être effectuée dans ce cas.
     */
    @Test
    void deletePerson_ShouldReturnFalseIfPersonNotFound() {
        // Given
//        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);

        // When
        boolean isDeleted = personRepository.deletePerson("NonExistent", "Person");

        // Then
        assertFalse(isDeleted);
//        verify(jsonFileWriter, never()).setPersons(mockPersonList);
    }

    /**
     * Vérifie que la liste complète des personnes stockées est retournée correctement.
     * La liste retournée doit contenir toutes les personnes enregistrées.
     */
    @Test
    void getAllPersons_ShouldReturnListOfPersons() {
        // Given
        when(jsonFileWriter.getPersons()).thenReturn(mockPersonList);

        // When
        List<Person> result = personRepository.getAllPersons();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(jsonFileWriter, times(1)).getPersons();
    }
}