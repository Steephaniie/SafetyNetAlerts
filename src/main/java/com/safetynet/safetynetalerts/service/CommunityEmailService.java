package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityEmailService {

    private final PersonService personService;

    public CommunityEmailService(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Récupère les adresses email de toutes les personnes vivant dans une ville spécifiée.
     *
     * @param city La ville pour laquelle récupérer les adresses email.
     * @return Un DTO contenant la ville et les emails associés.
     */
    public CommunityEmailDTO getEmailsByCity(String city) {
        // Récupérer toutes les personnes
        List<Person> allPersons = personService.getAllPersons();

        // Filtrer les personnes vivant dans la ville spécifiée et récupérer leurs emails
        List<String> emails = allPersons.stream()
                .filter(person -> city.equalsIgnoreCase(person.getCity()))
                .map(Person::getEmail)
                .filter(email -> email != null && !email.isEmpty()) // Exclure les emails nuls ou vides
                .distinct() // Éviter les doublons
                .collect(Collectors.toList());

        return new CommunityEmailDTO(city, emails);
    }
}