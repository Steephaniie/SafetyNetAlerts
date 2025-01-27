package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireStationService {

    private final FireStationRepository fireStationRepository;

    public FireStationService(FireStationRepository fireStationRepository) {
        this.fireStationRepository = fireStationRepository;
    }

    /**
     * Ajouter une nouvelle caserne de pompiers.
     *
     * @param fireStation La caserne à ajouter.
     */
    public void addFireStation(FireStation fireStation) {
        fireStationRepository.addFireStation(fireStation);
    }

    /**
     * Mettre à jour le numéro de station à une adresse donnée.
     *
     * @param address          L'adresse de la caserne à mettre à jour.
     * @param newStationNumber Le nouveau numéro de la caserne.
     * @return true si la mise à jour a été effectuée, false sinon.
     */
    public boolean updateFireStation(String address, String newStationNumber) {
        return fireStationRepository.updateFireStation(address, newStationNumber);
    }

    /**
     * Supprimer une caserne de pompiers à une adresse donnée.
     *
     * @param address L'adresse de la caserne à supprimer.
     * @return true si la suppression a été effectuée, false sinon.
     */
    public boolean deleteFireStation(String address) {
        return fireStationRepository.deleteFireStation(address);
    }

    /**
     * Récupérer toutes les casernes de pompiers.
     *
     * @return Une liste contenant toutes les casernes.
     */
    public List<FireStation> getAllFireStations() {
        return fireStationRepository.getAllFireStations();
    }
}