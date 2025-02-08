package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Test
    void addFireStation_ShouldReturnSuccess() throws Exception {
// Arrange
        FireStation fireStation = new FireStation("123 Main St", "1");
        doNothing().when(fireStationService).addFireStation(any(FireStation.class));

// Act & Assert
        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"123 Main St\", \"station\":\"1\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne ajoutée avec succès."));
    }

    @Test
    void updateFireStation_ShouldReturnSuccess() throws Exception {
        // Arrange
        String address = "123 Main St";
        String newStationNumber = "2";
        when(fireStationService.updateFireStation(address, newStationNumber)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/firestation")
                        .param("address", address)
                        .param("newStationNumber", newStationNumber))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne mise à jour avec succès."));
    }

    @Test
    void deleteFireStation_ShouldReturnSuccess() throws Exception {
        // Arrange
        String address = "123 Main St";
        Mockito.when(fireStationService.deleteFireStation(address)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/firestation")
                        .param("address", address))
                .andExpect(status().isOk())
                .andExpect(content().string("Caserne supprimée avec succès."));
    }

    @Test
    void getAllFireStations_ShouldReturnList() throws Exception {
        // Arrange
        FireStation fireStation1 = new FireStation("123 Main St", "1");
        FireStation fireStation2 = new FireStation("456 Maple Dr", "2");
        when(fireStationService.getAllFireStations()).thenReturn(List.of(fireStation1, fireStation2));

        // Act & Assert
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
                .andExpect(jsonPath("$[0].station").value("1"))
                .andExpect(jsonPath("$[1].address").value("456 Maple Dr"))
                .andExpect(jsonPath("$[1].station").value("2"));
    }
}
