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
@SpringBootTest
class FireStationServiceTest {

    @MockBean
    private FireStationRepository fireStationRepository;

    @Autowired
    private FireStationService fireStationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialiser les mocks
    }

    @Test
    void testAddFireStation() {
        FireStation fireStation = new FireStation(); // Vous pouvez définir des valeurs ici
        doNothing().when(fireStationRepository).addFireStation(fireStation);

        fireStationService.addFireStation(fireStation);

        verify(fireStationRepository, times(1)).addFireStation(fireStation);
    }

    @Test
    void testUpdateFireStation_Success() {
        String address = "123 Main St";
        String newStationNumber = "2";

        // Simuler que la mise à jour a réussi
        when(fireStationRepository.updateFireStation(address, newStationNumber)).thenReturn(true);

        boolean result = fireStationService.updateFireStation(address, newStationNumber);

        assertTrue(result);
        verify(fireStationRepository, times(1)).updateFireStation(address, newStationNumber);
    }

    @Test
    void testUpdateFireStation_Failure() {
        String address = "123 Main St";
        String newStationNumber = "2";

        // Simuler un échec de mise à jour
        when(fireStationRepository.updateFireStation(address, newStationNumber)).thenReturn(false);

        boolean result = fireStationService.updateFireStation(address, newStationNumber);

        assertFalse(result);
        verify(fireStationRepository, times(1)).updateFireStation(address, newStationNumber);
    }

    @Test
    void testDeleteFireStation_Success() {
        String address = "123 Main St";

        // Simuler que la suppression a réussi
        when(fireStationRepository.deleteFireStation(address)).thenReturn(true);

        boolean result = fireStationService.deleteFireStation(address);

        assertTrue(result);
        verify(fireStationRepository, times(1)).deleteFireStation(address);
    }

    @Test
    void testDeleteFireStation_Failure() {
        String address = "123 Main St";

        // Simuler un échec de suppression
        when(fireStationRepository.deleteFireStation(address)).thenReturn(false);

        boolean result = fireStationService.deleteFireStation(address);

        assertFalse(result);
        verify(fireStationRepository, times(1)).deleteFireStation(address);
    }

    @Test
    void testGetAllFireStations() {
        List<FireStation> mockFireStations = new ArrayList<>();
        mockFireStations.add(new FireStation()); // Ajout d'exemples, pouvez augmenter le nombre
        mockFireStations.add(new FireStation());

        // Simuler le retour des casernes
        when(fireStationRepository.getAllFireStations()).thenReturn(mockFireStations);

        List<FireStation> result = fireStationService.getAllFireStations();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(fireStationRepository, times(1)).getAllFireStations();
    }
}