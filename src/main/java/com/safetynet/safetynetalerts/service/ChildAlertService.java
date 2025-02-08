package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO.ChildInfo;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO.HouseholdMember;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import com.safetynet.safetynetalerts.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ChildAlertService {

    private final PersonService personService;
    private final MedicalRecordService medicalRecordService;

    public ChildAlertService(PersonService personService, MedicalRecordService medicalRecordService) {
        this.personService = personService;
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Récupère les informations sur les enfants et les membres du foyer pour une adresse donnée.
     *
     * @param address L'adresse pour laquelle rechercher les informations.
     * @return Un DTO contenant les enfants et les membres du foyer, ou une chaîne vide s'il n'y a pas d'enfants.
     */
    public ChildAlertDTO getChildrenAtAddress(String address) {
        log.debug("Début de la méthode getChildrenAtAddress avec l'adresse : {}", address); // Début de la méthode

        // Récupérer toutes les personnes à cette adresse
        List<Person> allPersons = personService.getAllPersons();
        List<Person> personsAtAddress = allPersons.stream()
                .filter(person -> address.equals(person.getAddress()))
                .collect(Collectors.toList());
        log.debug("Nombre de personnes trouvées à l'adresse {} : {}", address, personsAtAddress.size()); // Nombre de personnes trouvées

        // Récupérer les dossiers médicaux pour ces personnes
        List<MedicalRecord> allMedicalRecords = medicalRecordService.getAllMedicalRecords();

        List<ChildInfo> children = new ArrayList<>();
        List<HouseholdMember> householdMembers = new ArrayList<>();

        for (Person person : personsAtAddress) {
            log.debug("Recherche du dossier médical pour : {} {}", person.getFirstName(), person.getLastName()); // Recherche dossier médical
            MedicalRecord medicalRecord = allMedicalRecords.stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName())
                            && record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);
            log.debug("Personne trouvée : {} {}, Age calculé : {}",
                    person.getFirstName(), person.getLastName(),
                    (medicalRecord != null) ? calculateAge(medicalRecord.getBirthDate()) : "Pas de dossier médical");


//            if (medicalRecord != null) {
                log.debug("Dossier médical trouvé pour : {} {}", person.getFirstName(), person.getLastName()); // Succès recherche

                if (medicalRecord != null) {
                int age = calculateAge(medicalRecord.getBirthDate());
                if (age <= 18) {
                    // Ajouter l'enfant à la liste des enfants
                    children.add(new ChildInfo(person.getFirstName(), person.getLastName(), age));
                } else {
                    log.debug("{} {} est un adulte, ajout à householdMembers", person.getFirstName(), person.getLastName()); // Personne est adulte
                    householdMembers.add(new HouseholdMember(person.getFirstName(), person.getLastName()));
                }
                }
        }

        // Ajouter les adultes/foyer restants si les personnes sont listées comme enfants
        for (ChildInfo child : children) {
            personsAtAddress.stream()
                    .filter(person -> !child.getFirstName().equals(person.getFirstName())
                            || !child.getLastName().equals(person.getLastName()))
                    .forEach(person -> {
                        HouseholdMember member = new HouseholdMember(person.getFirstName(), person.getLastName());
                        if (!householdMembers.contains(member)) {
                            householdMembers.add(member);
                        }
                    });
        }

            // Ajouter un log du résultat final
            log.debug("Nombre d'enfants trouvés : {}", children.size()); // Log enfants
            log.debug("Nombre de membres du foyer trouvés : {}", householdMembers.size()); // Log membres foyer

            // Si aucun enfant n'est trouvé, retourner une chaîne vide
            if (children.isEmpty()) {
                log.debug("Aucun enfant trouvé à l'adresse : {}", address); // Aucun enfant trouvé
                return new ChildAlertDTO(new ArrayList<>(), new ArrayList<>());
            }

            // Retourner le DTO
            log.debug("Retour des données pour l'adresse {} : enfants = {}, membres du foyer = {}", address, children, householdMembers); // Log retour
            return new ChildAlertDTO(children, householdMembers);
        }

    /**
     * Méthode utilitaire pour calculer l'âge d'une personne à partir de sa date de naissance.
     *
     * @param birthDate La date de naissance.
     * @return L'âge en années.
     */
    int calculateAge(Date birthDate){
            if (birthDate == null) {
                log.debug("Date de naissance nulle, renvoi de l'âge 0"); // Date nulle
                return 0;
            }
        LocalDate birthLocalDate = birthDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
        int age = LocalDate.now().getYear() - birthLocalDate.getYear() -
                (LocalDate.now().getDayOfYear() < birthLocalDate.getDayOfYear() ? 1 : 0);

        log.debug("Calcul âge - Date de naissance : {}, Age calculé : {}", birthDate, age);
        return age;

    }
}