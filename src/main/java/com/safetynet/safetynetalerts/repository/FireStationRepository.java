package com.safetynet.safetynetalerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.dto.FichierJsonDTO;
import com.safetynet.safetynetalerts.model.FireStation;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class FireStationRepository {
    private List<FireStation> firestations;

    // Constructeur
    public FireStationRepository() {
        loadData();
    }

    // chargement des données depuis le fichier Json
    private void loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File("src/main/resources/data.json");
            // Lire les données et les convertir en listes
            FichierJsonDTO fichierJsonDTO = objectMapper.readValue(jsonFile, FichierJsonDTO.class);
            firestations = fichierJsonDTO.getFirestations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajouter une nouvelle caserne de pompiers.
     *
     * @param fireStation La nouvelle caserne de pompiers à ajouter.
     */
    public void addFireStation(FireStation fireStation) {
        firestations.add(fireStation);
    }

    /**
     * Mettre à jour le numéro de la caserne pour une adresse donnée.
     *
     * @param address          L'adresse à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return true si la mise à jour a réussi, false si l'adresse n'a pas été trouvée.
     */
    public boolean updateFireStation(String address, String newStationNumber) {
        Optional<FireStation> fireStationOptional = firestations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .findFirst();

        if (fireStationOptional.isPresent()) {
            fireStationOptional.get().setStation(newStationNumber);
            return true; // Mise à jour réussie
        }
        return false; // Adresse non trouvée
    }

    /**
     * Supprimer une caserne de pompiers à une adresse donnée.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return true si la caserne a été supprimée, false sinon.
     */
    public boolean deleteFireStation(String address) {
        return firestations.removeIf(fireStation -> fireStation.getAddress().equals(address));
    }

    /**
     * Obtenir la liste de toutes les casernes de pompiers.
     *
     * @return La liste des casernes de pompiers.
     */
    public List<FireStation> getAllFireStations() {
        return firestations;
    }

}



