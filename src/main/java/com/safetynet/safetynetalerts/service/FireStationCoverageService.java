package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO;
import com.safetynet.safetynetalerts.dto.FireStationCoverageDTO.PersonInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FireStationCoverageService {

    private final FireStationService fireStationService;
    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;

    /**
     * Récupère la couverture d'une caserne donnée par son numéro.
     *
     * @param stationNumber Numéro de la caserne.
     * @return Un DTO contenant la liste des personnes couvertes, et le nombre d'adultes/enfants.
     */
    public FireStationCoverageDTO getCoverageByStationNumber(String stationNumber) {
        log.debug("Début d'exécution de la méthode getCoverageByStationNumber avec stationNumber : {}", stationNumber);

        // Récupérer les adresses associées à la caserne
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        List<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Si aucune caserne ne correspond, lever une exception 404
        if (coveredAddresses.isEmpty()) {
            log.debug("Aucune caserne trouvée pour le numéro de station : {}", stationNumber);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fire station not found");
        }
        log.debug("Adresses couvertes récupérées pour la caserne {}: {}", stationNumber, coveredAddresses);

        // Récupérer toutes les personnes habitants aux adresses couvertes
        List<Person> allPersons = personService.getAllPersons();
        List<Person> coveredPersons = allPersons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .collect(Collectors.toList());

        log.debug("Personnes couvertes récupérées pour les adresses {}: {}", coveredAddresses, coveredPersons);

        // Récupérer les dossiers médicaux de toutes ces personnes
        List<MedicalRecord> allMedicalRecords = medicalRecordService.getAllMedicalRecords();

        int numberOfAdults = 0;
        int numberOfChildren = 0;
        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : coveredPersons) {
            MedicalRecord medicalRecord = allMedicalRecords.stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName()) &&
                            record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord != null) {
                int age = calculateAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    numberOfChildren++;
                } else {
                    numberOfAdults++;
                }
            }

            log.debug("Ajout des informations de la personne : {} {}", person.getFirstName(), person.getLastName());
            personInfos.add(new PersonInfo(
                    person.getFirstName(),
                    person.getLastName(),
                    person.getAddress(),
                    person.getPhone()
            ));
        }

        log.debug("Nombre total d'adultes : {}, Nombre total d'enfants : {}", numberOfAdults, numberOfChildren);
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