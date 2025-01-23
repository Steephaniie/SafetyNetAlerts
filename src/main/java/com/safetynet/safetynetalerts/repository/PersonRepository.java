package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FichierJsonDTO;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepository {
    private List<Person> persons;

   public PersonRepository() {
        loadData();
    }


    private void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/resources/data.json");
            // Lire les données et les convertir en listes
            FichierJsonDTO fichierJsonDTO = objectMapper.readValue(jsonFile, FichierJsonDTO.class);
            persons=fichierJsonDTO.getPersons();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajouter une nouvelle personne.
     * @param person La nouvelle personne à ajouter.
     * @return La personne ajoutée.
     */
    public Person addPerson(Person person) {
        persons.add(person);
        return person; // Retourne la personne après l'avoir ajoutée
    }
    /**
     * Met à jour une personne existante.
     * @param firstName Le prénom de la personne à mettre à jour.
     * @param lastName Le nom de famille de la personne à mettre à jour.
     * @param updatedPerson Objet contenant les nouvelles données.
     * @return La personne mise à jour ou null si elle n'existe pas.
     */
    public Person updatePerson(String firstName, String lastName, Person updatedPerson) {
        Optional<Person> existingPersonOpt = persons.stream()
                .filter(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName))
                .findFirst();

        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();

            // Mettre à jour les champs modifiables
            existingPerson.setAddress(updatedPerson.getAddress());
            existingPerson.setCity(updatedPerson.getCity());
            existingPerson.setZip(updatedPerson.getZip());
            existingPerson.setPhone(updatedPerson.getPhone());
            existingPerson.setEmail(updatedPerson.getEmail());

            return existingPerson; // Retourne la personne après la mise à jour
        }

        return null; // Retourne null si aucune personne correspondante n'est trouvée
    }

    /**
     * Supprimer une personne.
     * @param firstName Le prénom de la personne à supprimer.
     * @param lastName Le nom de famille de la personne à supprimer.
     * @return true si la personne a été supprimée, false sinon.
     */
    public boolean deletePerson(String firstName, String lastName) {
        int initialSize = persons.size();
        persons = persons.stream()
                .filter(p -> !(p.getFirstName().equals(firstName) && p.getLastName().equals(lastName)))
                .collect(Collectors.toList());

        return persons.size() < initialSize; // Retourne true si une personne a été supprimée
    }

    /**
     * Récupérer la liste de toutes les personnes.
     *
     * @return Liste des personnes.
     */
    public List<Person> getAllPersons() {
        return persons;
    }


}
