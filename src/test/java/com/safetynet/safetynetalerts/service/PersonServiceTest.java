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


/**
 * Classe de test pour le service {@link PersonService}.
 * Cette classe teste les fonctionnalités principales associées à la gestion des objets {@link Person}.
 * Les tests vérifient le comportement attendu pour des cas réussis et des cas d'échec.
 */
@SpringBootTest
class PersonServiceTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    /**
     * Teste la méthode addPerson lors d'un ajout réussi.
     * Vérifie que la personne ajoutée est correctement enregistrée dans le dépôt.
     */
    @Test
    void testAddPerson_Success() {
        // Préparation des données et des comportements simulés (Arrange)
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "0123456789", "email@example.com");
        when(personRepository.addPerson(person)).thenReturn(person);

        // Exécution de la méthode à tester (Act)
        Person result = personService.addPerson(person);

        // Vérification des résultats et des comportements attendus (Assert)
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(personRepository, times(1)).addPerson(person);
    }

    /**
     * Teste la méthode addPerson en cas d'erreur liée à des noms null.
     * Vérifie qu'une exception est levée et que personne n'est ajouté au dépôt.
     */
   @Test
    void testAddPerson_Failure_NullName() {
        // Préparation des données avec un prénom null (Arrange)
        Person person = new Person(null, "Doe", "123 Main St", "City", "12345", "0123456789", "email@example.com");

        // Exécution et vérification simultanée de l'exception levée (Act & Assert)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> personService.addPerson(person));
        assertEquals("Le prénom et le nom sont obligatoires", exception.getMessage());
        verify(personRepository, never()).addPerson(any());
    }

    /**
     * Teste la méthode updatePerson pour un scénario de mise à jour réussie.
     * Vérifie que les modifications sont correctement enregistrées dans le dépôt.
     */
    @Test
    void testUpdatePerson_Success() {
        // Préparation des données et comportement simulé (Arrange)
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "City", "67890", "0987654321", "newemail@example.com");
        when(personRepository.updatePerson("John", "Doe", updatedPerson)).thenReturn(updatedPerson);

        // Exécution de la méthode à tester (Act)
        Person result = personService.updatePerson("John", "Doe", updatedPerson);

        // Vérification des résultats (Assert)
        assertNotNull(result);
        assertEquals("456 Elm St", result.getAddress());
        verify(personRepository, times(1)).updatePerson("John", "Doe", updatedPerson);
    }

    /**
     * Teste la méthode updatePerson lorsqu'aucune personne correspondante n'est trouvée.
     * Vérifie que l'exception appropriée est levée.
     */
    @Test
    void testUpdatePerson_PersonNotFound() {
        // Préparation des données et du comportement simulé pour personne non trouvée (Arrange)
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "City", "67890", "0987654321", "newemail@example.com");
        when(personRepository.updatePerson("John", "Doe", updatedPerson)).thenReturn(null);

        // Exécution et vérification simultanée de l'exception (Act & Assert)
        Exception exception = assertThrows(RuntimeException.class, () -> personService.updatePerson("John", "Doe", updatedPerson));
        assertEquals("Personne non trouvée pour la mise à jour", exception.getMessage());
        verify(personRepository, times(1)).updatePerson("John", "Doe", updatedPerson);
    }

    /**
     * Teste la méthode deletePerson lorsqu'une suppression réussie est effectuée.
     * Vérifie que la personne est correctement supprimée.
     */
    @Test
    void testDeletePerson_Success() {
        // Préparation du comportement simulé pour suppression réussie (Arrange)
        when(personRepository.deletePerson("John", "Doe")).thenReturn(true);

        // Exécution de la méthode à tester (Act)
        boolean result = personService.deletePerson("John", "Doe");

        // Vérification des résultats (Assert)
        assertTrue(result);
        verify(personRepository, times(1)).deletePerson("John", "Doe");
    }

    /**
     * Teste la méthode deletePerson lorsqu'aucune personne correspondante n'est trouvée.
     * Vérifie que la suppression échoue et que le résultat est false.
     */
    @Test
    void testDeletePerson_Failure_NotFound() {
        // Arrange
        when(personRepository.deletePerson("John", "Doe")).thenReturn(false);

        // Act & Assert
        assertFalse(personService.deletePerson("John", "Doe"));
        verify(personRepository, times(1)).deletePerson("John", "Doe");
    }

    /**
     * Teste la méthode getAllPersons pour une récupération réussie de toutes les personnes.
     * Vérifie que la liste contient deux personnes et que les données sont valides.
     */
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

    /**
     * Teste la méthode getAllPersons lorsqu'aucune personne n'est disponible.
     * Vérifie qu'une liste vide est renvoyée.
     */
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