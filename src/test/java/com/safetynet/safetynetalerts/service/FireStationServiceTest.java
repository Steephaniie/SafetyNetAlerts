package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Teste les fonctionnalités du service FireStationService.
 * Vérifie les opérations CRUD sur les casernes de pompiers
 * en utilisant des mocks de FireStationRepository.
 */
@SpringBootTest
class FireStationServiceTest {

    @MockBean
    private FireStationRepository fireStationRepository;

    @Autowired
    private FireStationService fireStationService;

    /**
     * Initialise les mocks avant chaque test.
     * Pré-requis pour simuler les dépendances de FireStationService.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    /**
     * Teste l'ajout d'une nouvelle caserne de pompiers.
     * Vérifie que le service appelle correctement le dépôt (repository)
     * pour ajouter une caserne à partir des données fournies.
     */
    @Test
    void testAddFireStation() {
        FireStation fireStation = new FireStation(); // Caserne fictive pour le test
        doNothing().when(fireStationRepository).addFireStation(fireStation);

        // Appelle le service pour ajouter la caserne
        fireStationService.addFireStation(fireStation);

        // Vérifie que la méthode du dépôt a été appelée une fois avec les bonnes données
        verify(fireStationRepository, times(1)).addFireStation(fireStation);
    }

    /**
     * Teste la mise à jour réussie d'une caserne de pompiers.
     * Vérifie qu'une mise à jour effectuée avec des données valides retourne "true".
     */
    @Test
    void testUpdateFireStation_Success() {
        String address = "123 Main St"; // Adresse fictive à mettre à jour
        String newStationNumber = "2"; // Nouveau numéro de caserne

        // Simule un scénario où la mise à jour réussit dans le dépôt
        when(fireStationRepository.updateFireStation(address, newStationNumber)).thenReturn(true);

        // Effectue la mise à jour et stocke le résultat
        boolean result = fireStationService.updateFireStation(address, newStationNumber);

        // Vérifie que le résultat est vrai et que la méthode du dépôt a été appelée une fois
        assertTrue(result);
        verify(fireStationRepository, times(1)).updateFireStation(address, newStationNumber);
    }

    /**
     * Teste un échec de mise à jour d'une caserne de pompiers.
     * Vérifie qu'une tentative de mise à jour avec des données valides retourne "false".
     */
    @Test
    void testUpdateFireStation_Failure() {
        String address = "123 Main St"; // Adresse fictive à mettre à jour
        String newStationNumber = "2"; // Nouveau numéro de caserne

        // Simule un scénario où la mise à jour échoue dans le dépôt
        when(fireStationRepository.updateFireStation(address, newStationNumber)).thenReturn(false);

        // Effectue la tentative de mise à jour et stocke le résultat
        boolean result = fireStationService.updateFireStation(address, newStationNumber);

        // Vérifie que le résultat est faux et que la méthode du dépôt a été appelée une fois
        assertFalse(result);
        verify(fireStationRepository, times(1)).updateFireStation(address, newStationNumber);
    }

    /**
     * Teste la suppression réussie d'une caserne de pompiers.
     * Vérifie qu'une méthode retourne "true" après suppression d'une caserne avec les données fournies.
     */
    @Test
    void testDeleteFireStation_Success() {
        String address = "123 Main St"; // Adresse fictive à supprimer

        // Simule un scénario où la suppression réussit dans le dépôt
        when(fireStationRepository.deleteFireStation(address)).thenReturn(true);

        // Effectue la suppression et stocke le résultat
        boolean result = fireStationService.deleteFireStation(address);

        // Vérifie que le résultat est vrai et que la méthode du dépôt a été appelée une fois
        assertTrue(result);
        verify(fireStationRepository, times(1)).deleteFireStation(address);
    }

    /**
     * Teste un échec de suppression d'une caserne de pompiers.
     * Vérifie qu'une tentative de suppression retourne "false" lorsque la suppression échoue.
     */
    @Test
    void testDeleteFireStation_Failure() {
        String address = "123 Main St"; // Adresse fictive à supprimer

        // Simule un scénario où la suppression échoue dans le dépôt
        when(fireStationRepository.deleteFireStation(address)).thenReturn(false);

        // Effectue la tentative de suppression et stocke le résultat
        boolean result = fireStationService.deleteFireStation(address);

        // Vérifie que le résultat est faux et que la méthode du dépôt a été appelée une fois
        assertFalse(result);
        verify(fireStationRepository, times(1)).deleteFireStation(address);
    }

    /**
     * Teste la récupération de toutes les casernes de pompiers.
     * Vérifie que le service retourne une liste contenant toutes les casernes du dépôt.
     */
    @Test
    void testGetAllFireStations() {
        List<FireStation> mockFireStations = new ArrayList<>(); // Liste fictive pour le test
        mockFireStations.add(new FireStation()); // Exemple de caserne
        mockFireStations.add(new FireStation()); // Deuxième exemple de caserne

        // Simule un retour du dépôt avec la liste fictive
        when(fireStationRepository.getAllFireStations()).thenReturn(mockFireStations);

        // Appelle la méthode du service pour récupérer les casernes
        List<FireStation> result = fireStationService.getAllFireStations();

        // Vérifie que la liste retournée n'est pas nulle, contient 2 entrées et que le dépôt a été appelé une fois
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fireStationRepository, times(1)).getAllFireStations();
    }
}