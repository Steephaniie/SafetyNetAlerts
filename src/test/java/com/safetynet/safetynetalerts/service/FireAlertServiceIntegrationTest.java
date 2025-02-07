package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertDTO.ResidentInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class FireAlertServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private FireStationService fireStationService;

    @InjectMocks
    private FireAlertService fireAlertService;

    @Test
    void testGetFireAlertByAddress() {
        // Initialisation des mocks
        MockitoAnnotations.openMocks(this);

        // Mock des données d'entrée
        String address = "123 Main St";
        FireStation fireStation = new FireStation();
        fireStation.setStation("3");
        List<FireStation> fireStations = new java.util.ArrayList<>();
        fireStations.add(fireStation);

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPhone("123-456-7890");
        person.setAddress(address);

        Date birthDate = Date.from(
            LocalDate.of(1985, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthdate(birthDate);
        medicalRecord.setMedications(List.of("med1", "med2"));
        medicalRecord.setAllergies(List.of("allergy1"));

        List<MedicalRecord> medicalRecords= new java.util.ArrayList<>();
        medicalRecords.add(medicalRecord);

        // Mock des services
        when(personService.getAllPersons()).thenReturn(List.of(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);
        when(fireStationService.getAllFireStations()).thenReturn(fireStations);

        // Appel de la méthode
        FireAlertDTO result = fireAlertService.getFireAlertByAddress(address);

        // Assertions
        assertEquals(fireStation.getStation(), result.getFireStationNumber());
        assertEquals(1, result.getResidents().size());

        ResidentInfo resident = result.getResidents().get(0);
        assertEquals("John Doe", resident.getFirstName()+" "+resident.getLastName() );
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(40, resident.getAge()); // Exemple avec l'année actuelle
        assertEquals(List.of("med1", "med2"), resident.getMedications());
        assertEquals(List.of("allergy1"), resident.getAllergies());
    }
}