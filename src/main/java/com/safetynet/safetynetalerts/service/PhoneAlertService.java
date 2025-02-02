package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PhoneAlertDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneAlertService {

    private final FireStationService fireStationService;
    private final PersonService personService;

    @Autowired
    public PhoneAlertService(FireStationService fireStationService, PersonService personService) {
        this.fireStationService = fireStationService;
        this.personService = personService;
    }

    /**
     * Récupère la liste des numéros de téléphone des résidents couverts par une caserne spécifique.
     *
     * @param stationNumber Le numéro de la caserne.
     * @return Un DTO contenant les numéros de téléphone.
     */
    public PhoneAlertDTO getPhonesByFireStation(String stationNumber) {
        // Récupérer les adresses associées à la caserne
        List<FireStation> fireStations = fireStationService.getAllFireStations();
        List<String> addresses = fireStations.stream()
                .filter(fireStation -> fireStation.getStation().equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toList());

        // Récupérer les personnes habitant à ces adresses et leurs numéros de téléphone
        List<Person> persons = personService.getAllPersons();
        List<String> phoneNumbers = persons.stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct() // Éviter les doublons
                .collect(Collectors.toList());

        return new PhoneAlertDTO(phoneNumbers);
    }
}