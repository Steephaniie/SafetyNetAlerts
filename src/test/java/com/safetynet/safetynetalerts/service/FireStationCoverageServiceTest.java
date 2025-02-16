package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
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
 * Classe de test pour valider les fonctionnalités du service FireStationCoverageService.
 * Utilise des mocks pour simuler les dépendances et effectuer des tests unitaires.
 */
@SpringBootTest
public class FireStationCoverageServiceTest {
    @MockBean
    private  FireStationService fireStationService;
    @MockBean
    private PersonService personService;
    @MockBean
    private MedicalRecordService medicalRecordService;
    @Autowired
    private FireStationCoverageService fireStationCoverageService;

    /**
     * Méthode exécutée avant chaque test pour initialiser les mocks.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test permettant de vérifier la couverture par numéro de caserne.
     * Ce test valide que le service retourne les bonnes informations, y compris le
     * nombre d'adultes, d'enfants, et les détails des personnes vivant à l'adresse concernée.
     *
     * @throws Exception si une erreur survient dans l'exécution du service
     */
    @Test
    public void testGetCoverageByStationNumber() throws Exception {
        // Mock des données d'entrée : création d'une caserne de pompiers
        String address = "123 Main St";
        FireStation fireStation = new FireStation();
        fireStation.setStation("3");
        fireStation.setAddress(address);
        // Liste simulée de casernes avec une seule caserne spécifiée
        List<FireStation> fireStations = new java.util.ArrayList<>();
        fireStations.add(fireStation);

        // Création d'une personne fictive à l'adresse de la caserne
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPhone("123-456-7890");
        person.setAddress(address);

        // Date de naissance utilisée pour déterminer si la personne est adulte ou enfant
        Date birthDate = Date.from(
                LocalDate.of(1985, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        // Création d'un dossier médical fictif lié à la personne
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Doe");
        medicalRecord.setBirthDate(birthDate);
        medicalRecord.setMedications(List.of("med1", "med2"));
        medicalRecord.setAllergies(List.of("allergy1"));

        // Liste simulée contenant le dossier médical de la personne
        List<MedicalRecord> medicalRecords = new java.util.ArrayList<>();
        medicalRecords.add(medicalRecord);


        when(fireStationService.getAllFireStations()).thenReturn(fireStations);
        when(personService.getAllPersons()).thenReturn(List.of(person));
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(medicalRecords);

        // Résultat retourné par le service pour le numéro de caserne "3"
        FireStationCoverageDTO result = fireStationCoverageService.getCoverageByStationNumber("3");

        // Vérifications des propriétés retournées par le service
        // Vérifie que le nombre d'adultes est correct (1 adulte attendu)
        assertEquals(1, result.getNumberOfAdults(), "Le nombre d'adultes doit être 1.");
        assertEquals(0, result.getNumberOfChildren(), "Le nombre d'enfants doit être 0.");
        assertEquals(1, result.getPersons().size(), "Le nombre total de personnes couvertes doit être 1.");
        assertEquals("John", result.getPersons().get(0).getFirstName(), "Le prénom de la personne doit être John.");
        assertEquals("Doe", result.getPersons().get(0).getLastName(), "Le nom de famille doit être Doe.");
        assertEquals("123-456-7890", result.getPersons().get(0).getPhone(), "Le numéro de téléphone doit correspondre.");

    }
}
