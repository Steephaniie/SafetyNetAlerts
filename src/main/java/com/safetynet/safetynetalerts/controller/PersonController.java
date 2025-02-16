package com.safetynet.safetynetalerts.controller;

import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/person")
@Tag(name = "Person Controller", description = "Gestion des informations des personnes.")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * Endpoint GET : Récupérer toutes les personnes.
     *
     * @return La liste de toutes les personnes.
     */
    @GetMapping
    @Operation(summary = "Récupérer toutes les personnes", description = "Retourne une liste contenant toutes les personnes enregistrées.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès.")
    })
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        log.info("api getAllPersons ok");
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    /**
     * Endpoint POST : Ajouter une nouvelle personne.
     *
     * @param person La personne à ajouter.
     * @return La personne ajoutée avec un statut HTTP 201.
     */
    @PostMapping
    @Operation(summary = "Ajouter une nouvelle personne", description = "Ajoute une personne avec les informations spécifiées dans la requête.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personne ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<Person> addPerson(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Détails de la personne à ajouter.")
            @Valid @RequestBody Person person) {
        try {
            Person createdPerson = personService.addPerson(person);
            log.info("api addPerson ok");
            return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("api addPerson ko" + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint PUT : Mettre à jour une personne existante.
     *
     * @param firstName     Le prénom de la personne.
     * @param lastName      Le nom de la personne.
     * @param updatedPerson Les nouvelles données pour la personne.
     * @return La personne mise à jour ou un statut HTTP 404 si la personne n'existe pas.
     */
    @PutMapping("/{firstName}/{lastName}")
    @Operation(summary = "Mettre à jour une personne", description = "Mise à jour des informations d'une personne existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personne mise à jour avec succès."),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée.")
    })
    public ResponseEntity<Person> updatePerson(
            @Parameter(description = "Prénom de la personne à mettre à jour.")
            @PathVariable String firstName,
            @Parameter(description = "Nom de famille de la personne à mettre à jour.")
            @PathVariable String lastName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nouvelles informations pour la personne.")
            @RequestBody Person updatedPerson) {

        Person updated = personService.updatePerson(firstName, lastName, updatedPerson);

        if (updated == null) {
            log.error("api updatePerson ko - Personne non trouvée");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        log.info("api updatePerson ok");
        return ResponseEntity.ok(updated);
    }

    /**
     * Endpoint DELETE : Supprimer une personne.
     *
     * @param firstName Le prénom de la personne à supprimer.
     * @param lastName  Le nom de la personne à supprimer.
     * @return Un statut HTTP 200 si la personne est supprimée, ou 404 si elle n'existe pas.
     */
    @DeleteMapping("/{firstName}/{lastName}")
    @Operation(summary = "Supprimer une personne", description = "Supprime les informations d'une personne en fonction de son prénom et nom de famille.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personne supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée.")
    })
    public ResponseEntity<String> deletePerson(
            @Parameter(description = "Prénom de la personne à supprimer.")
            @PathVariable String firstName,
            @Parameter(description = "Nom de famille de la personne à supprimer.")
            @PathVariable String lastName) {
        boolean deleted = personService.deletePerson(firstName, lastName);
        if (deleted) {
            log.info("api deletePerson ok");
            return new ResponseEntity<>("Personne supprimée avec succès.", HttpStatus.OK);
        } else {
            log.error("api deletePerson ko - Personne non trouvée");
            return new ResponseEntity<>("Personne non trouvée.", HttpStatus.NOT_FOUND);
        }
    }

}