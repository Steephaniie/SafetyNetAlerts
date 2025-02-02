package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.FireAlertDTO;
import com.safetynet.safetynetalerts.dto.FireAlertDTO.ResidentInfo;
import com.safetynet.safetynetalerts.model.FireStation;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FireAlertService {

    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;
    private final FireStationService fireStationService;

    public FireAlertService(PersonService personService, MedicalRecordService medicalRecordService, FireStationService fireStationService) {
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
        this.fireStationService = fireStationService;
    }

    /**
     * Récupère les détails des habitants et le numéro de caserne pour une adresse donnée.
     *
     * @param address L'adresse pour laquelle récupérer les informations.
     * @return Un DTO FireAlertDTO avec les informations des habitants et du numéro de caserne.
     */
    public FireAlertDTO getFireAlertByAddress(String address) {
        // Récupérer toutes les personnes habitant à cette adresse
        List<Person> personsAtAddress = personService.getAllPersons().stream()
                .filter(person -> address.equals(person.getAddress()))
                .collect(Collectors.toList());

        // Récupérer tous les dossiers médicaux pour les personnes de cette adresse
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

        // Trouver le numéro de la caserne couvrant cette adresse
        String fireStationNumber = fireStationService.getAllFireStations().stream()
                .filter(fireStation -> address.equals(fireStation.getAddress()))
                .map(FireStation::getStation)
                .findFirst()
                .orElse(null); // Adresse non couverte par une caserne

        // Construire la liste des habitants avec leurs informations détaillées
        List<ResidentInfo> residents = personsAtAddress.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName())
                                    && record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    if (medicalRecord != null) {
                        int age = calculateAge(medicalRecord.getBirthdate());
                        return new ResidentInfo(
                                person.getFirstName(),
                                person.getLastName(),
                                person.getPhone(),
                                age,
                                medicalRecord.getMedications(),
                                medicalRecord.getAllergies()
                        );
                    }
                    return new ResidentInfo(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getPhone(),
                            0, // Âge inconnu si aucun dossier médical trouvé
                            List.of(),
                            List.of()
                    );
                })
                .collect(Collectors.toList());

        // Retourner le DTO avec les informations collectées
        return new FireAlertDTO(fireStationNumber, residents);
    }

    /**
     * Méthode utilitaire pour calculer l'âge à partir d'une date de naissance.
     *
     * @param birthDate La date de naissance.
     * @return L'âge en années.
     */
    private int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return LocalDate.now().getYear() - birthLocalDate.getYear() -
                (LocalDate.now().getDayOfYear() < birthLocalDate.getDayOfYear() ? 1 : 0);
    }
}