package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.service.FireStationCoverageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

@WebMvcTest(FireStationCoverageController.class)
class FireStationCoverageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationCoverageService fireStationCoverageService;

    @Test
    void testGetCoverageByStationNumber_shouldReturnCoverage() throws Exception {
        // Arrange
        String stationNumber = "1";
        FireStationCoverageDTO mockResponse = new FireStationCoverageDTO();
        // Configure les données simulées
//        mockResponse.setStationNumber(stationNumber);
        mockResponse.setNumberOfAdults(5);
        mockResponse.setNumberOfChildren(2);

        when(fireStationCoverageService.getCoverageByStationNumber(stationNumber)).thenReturn(mockResponse);

        // Act and Assert
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfAdults").value(5))
                .andExpect(jsonPath("$.numberOfChildren").value(2));

    }

    @Test
    void testGetCoverageByStationNumber_shouldReturnBadRequestWhenStationNumberMissing() throws Exception {
        mockMvc.perform(get("/firestation"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCoverageByStationNumber_shouldReturn404WhenNotFound() throws Exception {
        // Arrange
        String stationNumber = "999";
        when(fireStationCoverageService.getCoverageByStationNumber(stationNumber))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire station not found"));

        // Act and Assert
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", stationNumber))
                .andExpect(status().isNotFound());
    }
}