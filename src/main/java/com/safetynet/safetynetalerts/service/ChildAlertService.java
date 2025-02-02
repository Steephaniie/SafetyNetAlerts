package com.safetynet.safetynetalerts.service;

import com.safetynet.safetynetalerts.dto.ChildAlertDTO;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO.ChildInfo;
import com.safetynet.safetynetalerts.dto.ChildAlertDTO.HouseholdMember;
import com.safetynet.safetynetalerts.model.Person;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        // Récupérer toutes les personnes à cette adresse
        List<Person> allPersons = personService.getAllPersons();
        List<Person> personsAtAddress = allPersons.stream()
                .filter(person -> address.equals(person.getAddress()))
                .collect(Collectors.toList());

        // Récupérer les dossiers médicaux pour ces personnes
        List<MedicalRecord> allMedicalRecords = medicalRecordService.getAllMedicalRecords();

        List<ChildInfo> children = new ArrayList<>();
        List<HouseholdMember> householdMembers = new ArrayList<>();

        for (Person person : personsAtAddress) {
            MedicalRecord medicalRecord = allMedicalRecords.stream()
                    .filter(record -> record.getFirstName().equals(person.getFirstName())
                            && record.getLastName().equals(person.getLastName()))
                    .findFirst()
                    .orElse(null);

            if (medicalRecord != null) {
                int age = calculateAge(medicalRecord.getBirthdate());
                if (age <= 18) {
                    // Ajouter l'enfant à la liste des enfants
                    children.add(new ChildInfo(person.getFirstName(), person.getLastName(), age));
                } else {
                    // Ajouter les adultes/foyer à la liste des membres
                    householdMembers.add(new HouseholdMember(person.getFirstName(), person.getLastName()));
                }
            }
        }

        // Ajouter les adultes/foyer restants si les personnes sont listées comme enfants
        for (ChildInfo child : children) {
            householdMembers.addAll(personsAtAddress.stream()
                    .filter(person -> !child.getFirstName().equals(person.getFirstName())
                            || !child.getLastName().equals(person.getLastName())) // Exclure l'enfant lui-même
                    .map(person -> new HouseholdMember(person.getFirstName(), person.getLastName()))
                    .collect(Collectors.toList()));
        }

        // Si aucun enfant n'est trouvé, retourner une chaîne vide
        if (children.isEmpty()) {
            return new ChildAlertDTO(new ArrayList<>(), new ArrayList<>());
        }

        // Retourner le DTO
        return new ChildAlertDTO(children, householdMembers);
    }

    /**
     * Méthode utilitaire pour calculer l'âge d'une personne à partir de sa date de naissance.
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