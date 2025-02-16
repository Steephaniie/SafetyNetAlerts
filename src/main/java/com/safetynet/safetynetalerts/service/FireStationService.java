package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FireStationService {

    private final FireStationRepository fireStationRepository;

    /**
     * Ajouter une nouvelle caserne de pompiers.
     *
     * @param fireStation La caserne à ajouter.
     */
    public void addFireStation(FireStation fireStation) {
        log.debug("Début de l'ajout d'une nouvelle caserne de pompiers : {}", fireStation);
        fireStationRepository.addFireStation(fireStation);
        log.debug("Fin de l'ajout de la caserne de pompiers : {}", fireStation);
    }

    /**
     * Mettre à jour le numéro de station à une adresse donnée.
     *
     * @param address          L'adresse de la caserne à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return true si la mise à jour a été effectuée, false sinon.
     */
    public boolean updateFireStation(String address, String newStationNumber) {
        log.debug("Début de la mise à jour de la caserne avec adresse : {} et nouveau numéro : {}", address, newStationNumber);
        boolean isUpdated = fireStationRepository.updateFireStation(address, newStationNumber);
        log.debug("Fin de la mise à jour de la caserne. Succès : {}", isUpdated);
        return isUpdated;
    }

    /**
     * Supprimer une caserne de pompiers à une adresse donnée.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return true si la suppression a été effectuée, false sinon.
     */
    public boolean deleteFireStation(String address) {
        log.debug("Début de la suppression de la caserne avec adresse : {}", address);
        boolean isDeleted = fireStationRepository.deleteFireStation(address);
        log.debug("Fin de la suppression de la caserne. Succès : {}", isDeleted);
        return isDeleted;
    }

    /**
     * Récupérer toutes les casernes de pompiers.
     *
     * @return Une liste contenant toutes les casernes.
     */
    public List<FireStation> getAllFireStations() {
        log.debug("Début de la récupération de toutes les casernes de pompiers.");
        List<FireStation> fireStations = fireStationRepository.getAllFireStations();
        log.debug("Fin de la récupération. Nombre de casernes retrouvées : {}", fireStations.size());
        return fireStations;
    }
}