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

/**
 * Classe de test pour PhoneAlertController.
 * Elle vérifie le comportement de l'endpoint lié aux alertes téléphoniques.
 */
class PhoneAlertControllerTest {

    @Mock
    private PhoneAlertService phoneAlertService;

    @InjectMocks
    private PhoneAlertController phoneAlertController;

    /**
     * Initialisation des mocks avant chaque test.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }

    /**
     * Vérifie que l'API retourne un code 200 (OK) avec les numéros de téléphone
     * correspondants lorsque des données sont disponibles.
     */
    @Test
    void getPhoneAlert_withPhoneNumbers_shouldReturnOkResponse() {
        // Arrange : Configurer les données d'entrée et le comportement du service mocké
        String firestation = "1";
        List<String> phoneNumbers = List.of("123-456-7890", "987-654-3210");
        PhoneAlertDTO phoneAlertDTO = new PhoneAlertDTO(phoneNumbers);

        when(phoneAlertService.getPhonesByFireStation(firestation)).thenReturn(phoneAlertDTO);

        // Act : Exécuter la méthode testée
        ResponseEntity<PhoneAlertDTO> response = phoneAlertController.getPhoneAlert(firestation);

        // Assert : Vérifier les résultats retournés
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(phoneAlertDTO, response.getBody());
    }

    /**
     * Vérifie que l'API retourne un code 204 (No Content) lorsqu'aucun numéro de
     * téléphone n'est disponible pour une caserne donnée.
     */
    @Test
    void getPhoneAlert_withoutPhoneNumbers_shouldReturnNoContentResponse() {
        // Arrange : Configurer les données pour simuler l'absence de résultats
        String firestation = "2";
        PhoneAlertDTO phoneAlertDTO = new PhoneAlertDTO(Collections.emptyList());

        when(phoneAlertService.getPhonesByFireStation(firestation)).thenReturn(phoneAlertDTO);

        // Act : Appeler la méthode cible
        ResponseEntity<PhoneAlertDTO> response = phoneAlertController.getPhoneAlert(firestation);

        // Assert : Vérifier que le statut retourné est bien 404
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}