package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.CommunityEmailDTO;
import com.safetynet.safetynetalerts.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Début de l'appel de getEmailsByCity pour la ville : {}", city);

        // Récupérer toutes les personnes
        List<Person> allPersons = personService.getAllPersons();
        log.debug("Nombre total de personnes récupérées : {}", allPersons.size());

        // Filtrer les personnes vivant dans la ville spécifiée et récupérer leurs emails
        List<String> emails = allPersons.stream()
                .filter(person -> city.equalsIgnoreCase(person.getCity()))
                .map(Person::getEmail)
                .filter(email -> email != null && !email.isEmpty()) // Exclure les emails nuls ou vides
                .distinct() // Éviter les doublons
                .collect(Collectors.toList());
        log.debug("Adresses email récupérées pour la ville {} : {}", city, emails);

        log.debug("Fin de l'exécution de getEmailsByCity pour la ville : {} avec {} emails trouvés.", city, emails.size());
        return new CommunityEmailDTO(city, emails);
    }
}