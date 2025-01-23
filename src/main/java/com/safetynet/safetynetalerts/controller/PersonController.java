package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Endpoint GET : Récupérer toutes les personnes.
     *
     * @return La liste de toutes les personnes.
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    /**
     * Endpoint POST : Ajouter une nouvelle personne.
     * @param person La personne à ajouter.
     * @return La personne ajoutée avec un statut HTTP 201.
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        try {
            Person createdPerson = personService.addPerson(person);
            return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint PUT : Mettre à jour une personne existante.
     * @param firstName Le prénom de la personne.
     * @param lastName Le nom de la personne.
     * @param updatedPerson Les nouvelles données pour la personne.
     * @return La personne mise à jour ou un statut HTTP 404 si la personne n'existe pas.
     */
    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<Person> updatePerson(
            @PathVariable String firstName,
            @PathVariable String lastName,
            @RequestBody Person updatedPerson) {
        try {
            Person updated = personService.updatePerson(firstName, lastName, updatedPerson);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404 si la personne n'existe pas
        }
    }

    /**
     * Endpoint DELETE : Supprimer une personne.
     * @param firstName Le prénom de la personne à supprimer.
     * @param lastName Le nom de la personne à supprimer.
     * @return Un statut HTTP 200 si la personne est supprimée, ou 404 si elle n'existe pas.
     */
    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<String> deletePerson(
            @PathVariable String firstName,
            @PathVariable String lastName) {
        try {
            personService.deletePerson(firstName, lastName);
            return new ResponseEntity<>("Personne supprimée avec succès.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Personne non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
    
}