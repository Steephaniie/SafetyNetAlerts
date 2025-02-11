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

/**
 * Classe de test pour {@link CommunityEmailService}.
 * Cette classe teste les fonctionnalités liées à la récupération des adresses e-mail des résidents d'une ville donnée.
 */
@SpringBootTest
class CommunityEmailServiceTest {

    @MockBean
    private PersonService personService;

    @Autowired
    private CommunityEmailService communityEmailService;

    @BeforeEach
    /**
     * Initialise les mocks avant chaque test.
     * Cette méthode configure Mockito pour l'utilisation des objets simulés.
     */
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation de Mockito
    }

    /**
     * Teste la méthode getEmailsByCity dans le cas où une ville valide est fournie.
     * Vérifie que les emails des personnes résidant dans la ville spécifiée sont correctement extraits.
     */
    @Test
    void testGetEmailsByCity_WithValidCity_ReturnsEmails() {
        // Arrange: Préparer la ville et les personnes associées
        String city = "Paris";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "la rue", "Paris", "34000", "0102030405", "john.doe@email.com"),
                new Person("Jane", "Smith", "la rue", "Paris", "34000", "0102030405", "jane.smith@email.com"),
                new Person("Mike", "Johnson", "la rue", "Lyon", "34000", "0102030405", "mike.johnson@email.com") // Autre ville
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act: Appeler la méthode pour obtenir les emails par ville
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert: Vérifier les résultats obtenus
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(2, result.getEmails().size());
        assertTrue(result.getEmails().contains("john.doe@email.com"));
        assertTrue(result.getEmails().contains("jane.smith@email.com"));

        // Vérifie que le service a bien été appelé une fois
        verify(personService, times(1)).getAllPersons();
    }

    /**
     * Teste la méthode getEmailsByCity lorsque aucune personne n'habite dans la ville spécifiée.
     * Vérifie que la liste des emails retournée est vide.
     */
    @Test
    void testGetEmailsByCity_WithNoMatchingCity_ReturnsEmptyList() {
        // Arrange: Préparer une ville pour laquelle il n'y a aucune correspondance
        String city = "Marseille";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "la rue", "Paris", "34000", "0102030405", "john.doe@email.com"),
                new Person("Jane", "Smith", "la rue", "Lyon", "34000", "0102030405", "jane.smith@email.com")
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act: Appeler la méthode pour obtenir les emails par ville
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert: Vérifier que la liste est vide
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertTrue(result.getEmails().isEmpty());

        // Vérifie que le service a bien été appelé une fois
        verify(personService, times(1)).getAllPersons();
    }

    /**
     * Teste la méthode getEmailsByCity en présence d'emails null ou vides.
     * Vérifie que seuls les emails valides sont pris en compte.
     */
    @Test
    void testGetEmailsByCity_WithNullEmails_IgnoresNullOrEmptyEmails() {
        // Arrange: Préparer des personnes avec certains emails nuls ou vides
        String city = "Paris";
        List<Person> persons = Arrays.asList(
                new Person("John", "Doe", "la rue", "Paris", "34000", "0102030405", null), // Email nul
                new Person("Jane", "Smith", "la rue", "Paris", "34000", "0102030405", ""), // Email vide
                new Person("Mike", "Brown", "la rue", "Paris", "34000", "0102030405", "mike.brown@email.com") // Email valide
        );
        when(personService.getAllPersons()).thenReturn(persons);

        // Act: Appeler la méthode pour obtenir les emails par ville
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert: Vérifier que seuls les emails valides sont inclus
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertEquals(1, result.getEmails().size());
        assertTrue(result.getEmails().contains("mike.brown@email.com"));

        // Vérifie que le service a bien été appelé une fois
        verify(personService, times(1)).getAllPersons();
    }

    /**
     * Teste la méthode getEmailsByCity si la liste des personnes est vide.
     * Vérifie que la liste des emails retournée est vide.
     */
    @Test
    void testGetEmailsByCity_WithEmptyPersonList_ReturnsEmptyList() {
        // Arrange: Simuler une liste vide de personnes
        String city = "Paris";
        when(personService.getAllPersons()).thenReturn(Collections.emptyList());

        // Act: Appeler la méthode pour obtenir les emails par ville
        CommunityEmailDTO result = communityEmailService.getEmailsByCity(city);

        // Assert: Vérifier que la liste des emails est vide
        assertNotNull(result);
        assertEquals(city, result.getCity());
        assertTrue(result.getEmails().isEmpty());

        // Vérifie que le service a bien été appelé une fois
        verify(personService, times(1)).getAllPersons();
    }
}