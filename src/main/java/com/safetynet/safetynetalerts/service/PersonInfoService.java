package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonInfoService {

    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;

    public PersonInfoService(PersonService personService, MedicalRecordService medicalRecordService) {
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Récupère les informations des personnes correspondant à un nom de famille donné.
     *
     * @param lastName Le nom de famille pour lequel chercher les informations.
     * @return Une liste de DTO PersonInfoDTO contenant les informations des personnes.
     */
    public List<PersonInfoDTO> getPersonInfoByLastName(String lastName) {
        // Récupérer toutes les personnes avec le nom spécifié
        List<Person> personsWithLastName = personService.getAllPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());

        // Récupérer tous les dossiers médicaux
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

        // Transformer Person + MedicalRecord en PersonInfoDTO
        return personsWithLastName.stream()
                .map(person -> {
                    // Trouver le dossier médical correspondant
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName())
                                    && record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    // Calculer l'âge depuis la date de naissance
                    int age = (medicalRecord != null) ? calculateAge(medicalRecord.getBirthdate()) : 0;

                    return new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            age,
                            person.getEmail(),
                            medicalRecord != null ? medicalRecord.getMedications(): List.of(),
                            medicalRecord != null ? medicalRecord.getAllergies(): List.of()
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Méthode utilitaire : Calcul de l'âge à partir d'une date de naissance.
     *
     * @param birthDate La date de naissance.
     * @return L'âge (en années).
     */
    private int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        return now.getYear() - birthLocalDate.getYear() -
                (now.getDayOfYear() < birthLocalDate.getDayOfYear() ? 1 : 0);
    }
}