package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.service.PhoneAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PhoneAlertControllerTest {

    @Mock
    private PhoneAlertService phoneAlertService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    @Test
    void getPhoneAlert_withPhoneNumbers_shouldReturnOkResponse() {
        // Arrange
        String firestation = "1";
        List<String> phoneNumbers = List.of("123-456-7890", "987-654-3210");
        PhoneAlertDTO phoneAlertDTO = new PhoneAlertDTO(phoneNumbers);

        when(phoneAlertService.getPhonesByFireStation(firestation)).thenReturn(phoneAlertDTO);

        // Act
        ResponseEntity<PhoneAlertDTO> response = phoneAlertController.getPhoneAlert(firestation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(phoneAlertDTO, response.getBody());
    }

    @Test
    void getPhoneAlert_withoutPhoneNumbers_shouldReturnNoContentResponse() {
        // Arrange
        String firestation = "2";
        PhoneAlertDTO phoneAlertDTO = new PhoneAlertDTO(Collections.emptyList());

        when(phoneAlertService.getPhonesByFireStation(firestation)).thenReturn(phoneAlertDTO);

        // Act
        ResponseEntity<PhoneAlertDTO> response = phoneAlertController.getPhoneAlert(firestation);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}