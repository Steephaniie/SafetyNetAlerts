package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.PersonInfoDTO;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Appel de la méthode getPersonInfoByLastName avec le nom de famille : {}", lastName);

        // Récupérer toutes les personnes avec le nom spécifié
        List<Person> personsWithLastName = personService.getAllPersons().stream()
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
        log.debug("Nombre de personnes trouvées avec le nom de famille {} : {}", lastName, personsWithLastName.size());

        // Récupérer tous les dossiers médicaux
        List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecords();

        // Transformer Person + MedicalRecord en PersonInfoDTO
        List<PersonInfoDTO> personInfoDTOs = personsWithLastName.stream()
                .map(person -> {
                    // Trouver le dossier médical correspondant
                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(record -> record.getFirstName().equals(person.getFirstName())
                                    && record.getLastName().equals(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    if (medicalRecord != null) {
                        log.debug("Dossier médical trouvé pour {} {} : {}", person.getFirstName(), person.getLastName(), medicalRecord);
                    } else {
                        log.debug("Aucun dossier médical trouvé pour {} {}", person.getFirstName(), person.getLastName());
                    }

                    // Calculer l'âge depuis la date de naissance
                    int age = (medicalRecord != null) ? calculateAge(medicalRecord.getBirthDate()) : 0;

                    PersonInfoDTO personInfoDTO = new PersonInfoDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            person.getAddress(),
                            age,
                            person.getEmail(),
                            medicalRecord != null ? medicalRecord.getMedications() : List.of(),
                            medicalRecord != null ? medicalRecord.getAllergies() : List.of()
                    );
                    log.debug("PersonInfoDTO créé : {}", personInfoDTO);
                    return personInfoDTO;
                })
                .collect(Collectors.toList());

        log.debug("Méthode getPersonInfoByLastName terminée, {} PersonInfoDTOs retournés", personInfoDTOs.size());
        return personInfoDTOs;
    }

    /**
     * Méthode utilitaire : Calcul de l'âge à partir d'une date de naissance.
     *
     * @param birthDate La date de naissance.
     * @return L'âge (en années).
     */
    int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();

        return now.getYear() - birthLocalDate.getYear() -
                (now.getDayOfYear() < birthLocalDate.getDayOfYear() ? 1 : 0);
    }
}