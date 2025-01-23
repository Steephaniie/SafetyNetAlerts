package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (person.getFirstName() == null || person.getLastName() == null) {
            throw new IllegalArgumentException("Le prénom et le nom sont obligatoires");
        }
        return personRepository.addPerson(person);
    }

    /**
     * Mettre à jour une personne existante.
     * @param firstName Le prénom de la personne à mettre à jour.
     * @param lastName Le nom de famille de la personne à mettre à jour.
     * @param updatedPerson Nouveaux détails de la personne.
     * @return La personne mise à jour.
     */
    public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
        Person updated = personRepository.updatePerson(firstName, lastName, updatedPerson);

        if (updated == null) {
            throw new RuntimeException("Personne non trouvée pour la mise à jour");
        }

        return updated;
    }

    /**
     * Supprimer une personne par son prénom et nom de famille.
     * @param firstName Le prénom de la personne.
     * @param lastName Le nom de famille de la personne.
     * @return true si la personne a été supprimée, false sinon.
     */
    public boolean deletePerson(String firstName, String lastName) {
        boolean isDeleted = personRepository.deletePerson(firstName, lastName);

        if (!isDeleted) {
            throw new RuntimeException("Échec de la suppression car la personne n'existe pas");
        }

        return true;
    }

    /**
     * Récupérer toutes les personnes.
     *
     * @return La liste de toutes les personnes.
     */
    public List<Person> getAllPersons() {
        return personRepository.getAllPersons();
    }
}