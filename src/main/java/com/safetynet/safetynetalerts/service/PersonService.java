package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Ajouter une nouvelle personne.
     * @param person La nouvelle personne à ajouter.
     * @return La personne ajoutée.
     */
    public Person addPerson(Person person) {
        log.debug("Ajout de la personne : {}", person);
        if (person.getFirstName() == null || person.getLastName() == null) {
            throw new IllegalArgumentException("Le prénom et le nom sont obligatoires");
        }
        Person addedPerson = personRepository.addPerson(person);
        log.debug("Personne ajoutée avec succès : {}", addedPerson);
        return addedPerson;
    }

    /**
     * Mettre à jour une personne existante.
     * @param firstName Le prénom de la personne à mettre à jour.
     * @param lastName Le nom de famille de la personne à mettre à jour.
     * @param updatedPerson Nouveaux détails de la personne.
     * @return La personne mise à jour.
     */
    public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
        log.debug("Mise à jour de la personne avec prénom : {}, nom : {} avec les détails : {}", firstName, lastName, updatedPerson);
        Person updated = personRepository.updatePerson(firstName, lastName, updatedPerson);

        if (updated == null) {
            throw new RuntimeException("Personne non trouvée pour la mise à jour");
        }

        log.debug("Personne mise à jour avec succès : {}", updated);
        return updated;
    }

    /**
     * Supprimer une personne par son prénom et nom de famille.
     * @param firstName Le prénom de la personne.
     * @param lastName Le nom de famille de la personne.
     * @return true si la personne a été supprimée, false sinon.
     */
    public boolean deletePerson(String firstName, String lastName) {
        log.debug("Suppression de la personne avec prénom : {}, nom : {}", firstName, lastName);
        boolean isDeleted = personRepository.deletePerson(firstName, lastName);

        if (!isDeleted) {
            log.debug("Personne non trouvée pour suppression : prénom={}, nom={}", firstName, lastName);
            return false;
        }

        log.debug("Personne supprimée avec succès : prénom : {}, nom : {}", firstName, lastName);
        return true;
    }

    /**
     * Récupérer toutes les personnes.
     *
     * @return La liste de toutes les personnes.
     */
     public List<Person> getAllPersons() {
         log.debug("Récupération de toutes les personnes");
         List<Person> allPersons = personRepository.getAllPersons();
         log.debug("Nombre total de personnes trouvées : {}", allPersons.size());
         return allPersons;
     }
}