package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO.PersonInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireStationCoverageService {

    private final FireStationService fireStationService;
    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;

    public FireStationCoverageService(FireStationService fireStationService, 
                                       PersonService personService,
                                       MedicalRecordService medicalRecordService) {
        this.fireStationService = fireStationService;
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Récupère la couverture d'une caserne donnée par son numéro.
     *
     * @param stationNumber Numéro de la caserne.
     * @return Un DTO contenant la liste des personnes couvertes, et le nombre d'adultes/enfants.
     */
    public FireStationCoverageDTO getCoverageByStationNumber(String stationNumber) {
        // Récupérer les adresses associées à la caserne
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        List<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Récupérer toutes les personnes habitants aux adresses couvertes
        List<Person> allPersons = personService.getAllPersons();
        List<Person> coveredPersons = allPersons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .collect(Collectors.toList());

        // Récupérer les dossiers médicaux de toutes ces personnes
        List<MedicalRecord> allMedicalRecords = medicalRecordService.getAllMedicalRecords();
        
        int numberOfAdults = 0;
        int numberOfChildren = 0;

        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : coveredPersons) {
            // Trouver le dossier médical de la personne
            MedicalRecord medicalRecord = allMedicalRecords.stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                            record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord != null) {
                // Calculer l'âge pour distinguer adultes et enfants
                int age = calculateAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    numberOfChildren++;
                } else {
                    numberOfAdults++;
                }
            }

            // Ajouter l'information de la personne à la liste
            personInfos.add(new PersonInfo(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()
            ));
        }

        // Créer et retourner le DTO
        return new FireStationCoverageDTO(personInfos, numberOfAdults, numberOfChildren);
    }

    /**
     * Méthode utilitaire pour calculer l'âge d'une personne à partir de sa date de naissance.
     *
     * @param birthDate La date de naissance.
     * @return L'âge en années.
     */
    private int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.now().getYear() - birthLocalDate.getYear() -
                (LocalDate.now().getDayOfYear() < birthLocalDate.getDayOfYear() ? 1 : 0);
    }
}