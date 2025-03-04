package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FloodStationsDTO;
import com.safetynet.safetynetalerts.dto.FloodStationsDTO.HouseholdInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FloodStationsService {

    private final PersonService personService;
    private final FireStationService fireStationService;
    private final MedicalRecordService medicalRecordService;

    /**
     * Récupère les foyers desservis par les casernes spécifiées.
     *
     * @param stationNumbers La liste des numéros de casernes.
     * @return Un DTO contenant les informations des foyers regroupés par adresse.
     */
    public FloodStationsDTO getHouseholdsByStations(List<String> stationNumbers) {
        log.debug("Appel de la méthode getHouseholdsByStations avec les numéros de casernes : {}", stationNumbers);
        // Récupérer toutes les casernes couvertes par les numéros spécifiés
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        List<String> coveredAddresses = fireStations.stream()
                .filter(fireStation -> stationNumbers.contains(fireStation.getStation())) // Casernes concernées
                .map(FireStation::getAddress) // Adresses couvertes
                .collect(Collectors.toList());
        log.debug("Adresses couvertes récupérées : {}", coveredAddresses);

        // Récupérer les personnes vivant à ces adresses
        List<Person> allPersons = personService.getAllPersons();
        List<Person> filteredPersons = allPersons.stream()
                .filter(person -> coveredAddresses.contains(person.getAddress()))
                .collect(Collectors.toList());
        log.debug("Personnes récupérées pour les adresses couvertes : {}", filteredPersons);

        // Récupérer tous les dossiers médicaux
        List<MedicalRecord> allMedicalRecords = medicalRecordService.getAllMedicalRecords();
        log.debug("Nombre total de dossiers médicaux récupérés : {}", allMedicalRecords.size());

        // Grouper les informations par adresse
        Map<String, List<HouseholdInfo>> householdsByAddress = filteredPersons.stream()
                .collect(Collectors.groupingBy(
                        Person::getAddress,  // Grouper par adresse
                        Collectors.mapping(person -> {  // Transformer les personnes en HouseholdInfo
                            MedicalRecord medicalRecord = allMedicalRecords.stream()
                                    .filter(record -> record.getFirstName().equals(person.getFirstName())
                                            && record.getLastName().equals(person.getLastName()))
                                    .findFirst()
                                    .orElse(null);

                            int age = medicalRecord != null ? calculateAge(medicalRecord.getBirthdate()) : 0;

                            return new HouseholdInfo(
                                    person.getFirstName(),
                                    person.getLastName(),
                                    person.getPhone(),
                                    age,
                                    medicalRecord != null ? medicalRecord.getMedications() : new ArrayList<>(),
                                    medicalRecord != null ? medicalRecord.getAllergies() : new ArrayList<>()
                            );
                        }, Collectors.toList())
                ));
        log.debug("Foyers regroupés par adresse : {}", householdsByAddress);

        FloodStationsDTO floodStationsDTO = new FloodStationsDTO(householdsByAddress);
        log.debug("Retour des informations des foyers desservis : {}", floodStationsDTO);
        return floodStationsDTO;
    }

    /**
     * Méthode utilitaire pour calculer l'âge à partir d'une date de naissance.
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