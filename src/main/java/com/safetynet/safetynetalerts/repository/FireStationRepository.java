package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationRepository {

    private final JsonFileWriter jsonFileWriter;

    public FireStationRepository(JsonFileWriter jsonFileWriter) {
        this.jsonFileWriter = jsonFileWriter;
    }

    /**
     * Ajouter une nouvelle caserne de pompiers.
     *
     * @param fireStation La nouvelle caserne de pompiers à ajouter.
     */
    public void addFireStation(FireStation fireStation) {

        List<FireStation> firestations = jsonFileWriter.getFirestations();
        firestations.add(fireStation);
        jsonFileWriter.setFirestations(firestations);
    }

    /**
     * Mettre à jour le numéro de la caserne pour une adresse donnée.
     *
     * @param address          L'adresse à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return true si la mise à jour a réussi, false si l'adresse n'a pas été trouvée.
     */
    public boolean updateFireStation(String address, String newStationNumber) {
        List<FireStation> firestations = jsonFileWriter.getFirestations();
        Optional<FireStation> fireStationOptional = firestations.stream()
                .filter(fireStation -> fireStation.getAddress().equals(address))
                .findFirst();

        if (fireStationOptional.isPresent()) {
            fireStationOptional.get().setStation(newStationNumber);
            jsonFileWriter.setFirestations(firestations);
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
        List<FireStation> firestations = jsonFileWriter.getFirestations();
        boolean resultat = firestations.removeIf(fireStation -> fireStation.getAddress().equals(address));
        jsonFileWriter.setFirestations(firestations);
        return resultat;
    }

    /**
     * Obtenir la liste de toutes les casernes de pompiers.
     *
     * @return La liste des casernes de pompiers.
     */
    public List<FireStation> getAllFireStations() {
        return jsonFileWriter.getFirestations();
    }

}



