package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.FireStation;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


/**
 * Classe repository pour gérer les casernes de pompiers avec les opérations CRUD.
 * Elle interagit avec le fichier JSON pour stocker et récupérer les données.
 */
@Service
public class FireStationRepository {

    private final JsonFileWriter jsonFileWriter;

    public FireStationRepository(JsonFileWriter jsonFileWriter) {
        this.jsonFileWriter = jsonFileWriter;
    }

    /**
     * Ajouter une nouvelle caserne de pompiers.
     * <p>
     * Cette méthode ajoute la caserne spécifiée à la liste des casernes existantes
     * et stockées dans le fichier JSON.
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
     * <p>
     * Cette méthode recherche une caserne basée sur l'adresse fournie. Si l'adresse existe,
     * le numéro de la caserne est mis à jour avec la valeur spécifiée.
     * La mise à jour est enregistrée dans le fichier JSON.
     *
     * @param address          L'adresse de la caserne à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return true si la mise à jour a réussi, false si aucune caserne à cette adresse n'a été trouvée.
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
     * <p>
     * Cette méthode supprime une caserne associée à l'adresse spécifiée si elle existe.
     * Les modifications sont ensuite enregistrées dans le fichier JSON.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return true si la caserne a été supprimée avec succès, false sinon.
     */
    public boolean deleteFireStation(String address) {
        List<FireStation> firestations = jsonFileWriter.getFirestations();
        boolean resultat = firestations.removeIf(fireStation -> fireStation.getAddress().equals(address));
        jsonFileWriter.setFirestations(firestations);
        return resultat;
    }

    /**
     * Obtenir la liste de toutes les casernes de pompiers.
     * <p>
     * Cette méthode retourne toutes les casernes de pompiers stockées dans le fichier JSON.
     *
     * @return La liste complète des casernes de pompiers.
     */
    public List<FireStation> getAllFireStations() {
        return jsonFileWriter.getFirestations();
    }

}



