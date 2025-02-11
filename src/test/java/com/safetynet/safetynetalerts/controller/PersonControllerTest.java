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


/**
 * Classe de test pour PersonController. Ces tests utilisent Mockito pour simuler le comportement
 * du service PersonService. Les méthodes de test couvrent les opérations CRUD sur les objets Person.
 */
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    /**
     * Initialisation des mocks avant chaque test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    };

    /**
     * Teste la méthode getAllPersons : vérifie que la liste des personnes est récupérée avec succès.
     */
    @Test
    void testGetAllPersons() {
        // Given : un service mocké qui retourne une liste de personnes fictives
        List<Person> mockPersons = Arrays.asList(
                new Person("John", "Doe","la rue", "Paris","34000","0102030405", "john.doe@email.com"),
                new Person("Jane", "Doe","la rue", "Lyon","34000","0102030405", "jane.smith@email.com")

        );
        when(personService.getAllPersons()).thenReturn(mockPersons);

        // When : on appelle la méthode du contrôleur pour récupérer toutes les personnes
        // When : on appelle la méthode du contrôleur pour ajouter une personne
        // When : on appelle la méthode du contrôleur pour mettre à jour une personne
        // When : on appelle la méthode du contrôleur pour mettre à jour cette personne
        // When : on appelle la méthode du contrôleur pour supprimer une personne
        // When : on appelle la méthode du contrôleur pour supprimer cette personne inexistante
        ResponseEntity<List<Person>> response = personController.getAllPersons();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPersons, response.getBody());
        verify(personService, times(1)).getAllPersons();
    }

    /**
     * Teste la méthode addPerson : vérifie qu'une personne peut être ajoutée avec succès.
     */
    @Test
    void testAddPerson() {
        // Given : une personne fictive et un service mocké prêt à ajouter cette personne
        Person newPerson =  new Person("John", "Doe","la rue", "Paris","34000","0102030405", "john.doe@email.com");

        when(personService.addPerson(newPerson)).thenReturn(newPerson);

        // When
        ResponseEntity<Person> response = personController.addPerson(newPerson);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newPerson, response.getBody());
        verify(personService, times(1)).addPerson(newPerson);
    }

    /**
     * Teste la méthode updatePerson : vérifie qu'un utilisateur peut être mis à jour avec succès.
     */
    @Test
    void testUpdatePerson() {
        // Given : une personne mise à jour et un service mocké capable de contenir cette mise à jour
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

    /**
     * Teste la méthode updatePerson dans le cas où la personne à mettre à jour n'est pas trouvée.
     */
    @Test
    void testUpdatePerson_NotFound() {
        // Given : une tentative de mise à jour pour une personne inexistante
        String firstName = "John";
        String lastName = "Doe";
        Person updatedPerson = new Person(firstName, lastName, "la rue", "Paris", "34000", "0102030405", "john.doe@email.com");
        when(personService.updatePerson(firstName, lastName, updatedPerson)).thenReturn(null);

        // When
        ResponseEntity<Person> response = personController.updatePerson(firstName, lastName, updatedPerson);

        // Then : on vérifie que le statut de la réponse est HTTP 200 (OK) 
        // et que le corps de la réponse contient bien la liste mockée
        // Then : on vérifie que le statut de la réponse est HTTP 201 (Created) 
        // et que le corps de la réponse correspond à la personne ajoutée
        // Then : on vérifie que le statut de la réponse est HTTP 200 (OK) 
        // et que le corps de la réponse correspond à l'utilisateur mis à jour
        // Then : on vérifie que le statut de la réponse est HTTP 404 (Not Found)
        // Then : on vérifie que le statut de la réponse est HTTP 200 (OK) et que 
        // le message retourné confirme la suppression
        // Then : on vérifie que le statut de la réponse est HTTP 404 (Not Found) 
        // avec un message indiquant que la personne n'a pas été trouvée
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(personService, times(1)).updatePerson(firstName, lastName, updatedPerson);
    }

    /**
     * Teste la méthode deletePerson : vérifie qu'une personne peut être supprimée avec succès.
     */
    @Test
    void testDeletePerson() {
        // Given : une personne existante à supprimer
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

    /**
     * Teste la méthode deletePerson dans le cas où la personne à supprimer n'est pas trouvée.
     */
    @Test
    void testDeletePerson_NotFound() {
        // Given : une tentative de suppression pour une personne inexistante
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