package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertDTO.ResidentInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Classe de test pour {@link FireAlertService}.
 * Elle utilise des mocks pour simuler les services dépendants :
 * {@link PersonService}, {@link MedicalRecordService} et {@link FireStationService}.
 * Les tests vérifient les fonctionnalités du service d'alerte incendie
 * en utilisant Spring Boot Test.
 */
@SpringBootTest
class FireAlertServiceTest {

    @MockBean
    private PersonService personService;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private FireStationService fireStationService;

    @Autowired
    private FireAlertService fireAlertService;

    /**
     * Teste la méthode {@link FireAlertService#getFireAlertByAddress(String)}.
     * Ce test vérifie que l'alerte incendie retourne des informations précises
     * pour une adresse couverte par une station de pompiers.
     * Les mocks simulent les données pour les détails des personnes, les dossiers médicaux et
     * les stations de pompiers. Les résultats sont vérifiés avec des assertions
     * basées sur les données d'entrée fictives.
     */
    @Test
    void testGetFireAlertByAddress() {
        // Initialisation des mocks pour permettre d'utiliser les services dépendants
        MockitoAnnotations.openMocks(this);

        // Données d'entrée fictives pour simuler une adresse couverte par une station de pompiers
        String address = "123 Main St";
        FireStation fireStation = new FireStation();
        fireStation.setStation("3");
        fireStation.setAddress(address);
        List<FireStation> fireStations = new java.util.ArrayList<>();
        fireStations.add(fireStation);

        // Création d'une personne fictive résidant à l'adresse testée
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPhone("123-456-7890");
        person.setAddress(address);

        Date birthDate = Date.from(
            LocalDate.of(1985, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        // Création d'un dossier médical fictif pour le résident
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthDate(birthDate);
        medicalRecord.setMedications(List.of("med1", "med2"));
        medicalRecord.setAllergies(List.of("allergy1"));

        List<MedicalRecord> medicalRecords= new java.util.ArrayList<>();
        medicalRecords.add(medicalRecord);

        // Configuration des mocks pour simuler les réponses des services
        when(personService.getAllPersons()).thenReturn(List.of(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);

        // Appel de la méthode testée
        FireAlertDTO result = fireAlertService.getFireAlertByAddress(address);

        // Vérification des résultats en comparant avec les données attendues
        assertEquals(fireStation.getStation(), result.getFireStationNumber());
        assertEquals(1, result.getResidents().size());

        ResidentInfo resident = result.getResidents().get(0);
        assertEquals("John Doe", resident.getFirstName()+" "+resident.getLastName() );
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(40, resident.getAge()); // L'âge du résident est calculé en fonction de sa date de naissance
        assertEquals(List.of("med1", "med2"), resident.getMedications());
        assertEquals(List.of("allergy1"), resident.getAllergies());
    }
}