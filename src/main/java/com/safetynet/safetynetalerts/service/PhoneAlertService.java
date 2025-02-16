package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PhoneAlertService {

    private final FireStationService fireStationService;
    private final PersonService personService;

    /**
     * Récupère la liste des numéros de téléphone des résidents couverts par une caserne spécifique.
     *
     * @param stationNumber Le numéro de la caserne.
     * @return Un DTO contenant les numéros de téléphone.
     */
    public PhoneAlertDTO getPhonesByFireStation(String stationNumber) {
        log.debug("Appel de getPhonesByFireStation avec stationNumber: {}", stationNumber);

        // Récupérer les adresses associées à la caserne
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        List<String> addresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());
        log.debug("Adresses récupérées pour la caserne {}: {}", stationNumber, addresses);

        // Récupérer les personnes habitant à ces adresses et leurs numéros de téléphone
        List<Person> persons = personService.getAllPersons();
        List<String> phoneNumbers = persons.stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct() // Éviter les doublons
                .collect(Collectors.toList());
        log.debug("Numéros de téléphone récupérés pour la caserne {}: {}", stationNumber, phoneNumbers);

        return new PhoneAlertDTO(phoneNumbers);
    }
}