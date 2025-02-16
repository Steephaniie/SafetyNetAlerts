package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.FireStation;
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
class FireStationRepositoryTest {

    @MockBean
    private JsonFileWriter jsonFileWriter;

    @Autowired
    private FireStationRepository fireStationRepository;

    private List<FireStation> fireStations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // données factices pour les tests
        fireStations = new ArrayList<>();
        fireStations.add(new FireStation("123 Main St", "1"));
        fireStations.add(new FireStation("456 Maple St", "2"));
    }

    @Test
    void testAddFireStation() {
        // Arrange
        FireStation newStation = new FireStation("789 Oak St", "3");
        when(jsonFileWriter.getFirestations()).thenReturn(fireStations);

        // Act
        fireStationRepository.addFireStation(newStation);

        // Assert
        verify(jsonFileWriter, times(1)).setFirestations(fireStations);
        assertTrue(fireStations.contains(newStation));
    }

    @Test
    void testUpdateFireStation_Success() {
        // Arrange
        String targetAddress = "123 Main St";
        String newStationNumber = "4";
        when(jsonFileWriter.getFirestations()).thenReturn(fireStations);

        // Act
        boolean result = fireStationRepository.updateFireStation(targetAddress, newStationNumber);

        // Assert
        assertTrue(result);
        assertEquals("4", fireStations.get(0).getStation());
        verify(jsonFileWriter, times(1)).setFirestations(fireStations);
    }

    @Test
    void testUpdateFireStation_NotFound() {
        // Arrange
        String targetAddress = "999 Unknown St";
        String newStationNumber = "5";
        when(jsonFileWriter.getFirestations()).thenReturn(fireStations);

        // Act
        boolean result = fireStationRepository.updateFireStation(targetAddress, newStationNumber);

        // Assert
        assertFalse(result);
        verify(jsonFileWriter, never()).setFirestations(any());
    }

    @Test
    void testDeleteFireStation_Success() {
        // Arrange
        String targetAddress = "123 Main St";
        when(jsonFileWriter.getFirestations()).thenReturn(fireStations);

        // Act
        boolean result = fireStationRepository.deleteFireStation(targetAddress);

        // Assert
        assertTrue(result);
        assertEquals(1, fireStations.size());
        verify(jsonFileWriter, times(1)).setFirestations(fireStations);
    }

    @Test
    void testDeleteFireStation_NotFound() {
        // Arrange
        String targetAddress = "999 Unknown St";

        // Act
        boolean result = fireStationRepository.deleteFireStation(targetAddress);

        // Assert
        assertFalse(result);
        assertEquals(2, fireStations.size());
    }

    @Test
    void testGetAllFireStations() {
        // Arrange
        when(jsonFileWriter.getFirestations()).thenReturn(fireStations);

        // Act
        List<FireStation> result = fireStationRepository.getAllFireStations();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(fireStations, result);
    }
}