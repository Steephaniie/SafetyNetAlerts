package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.json.JsonFileWriter;
import com.safetynet.safetynetalerts.model.MedicalRecord;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordRepository {
    private final JsonFileWriter jsonFileWriter;

    public MedicalRecordRepository(JsonFileWriter jsonFileWriter) {
        this.jsonFileWriter = jsonFileWriter;
    }
//    private List<MedicalRecord> medicalrecords;


    /**
     * Ajouter un dossier médical
     * @param medicalRecord Le dossier médical à ajouter
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> medicalrecords= jsonFileWriter.getMedicalrecords();
        medicalrecords.add(medicalRecord);
        jsonFileWriter.setMedicalrecords(medicalrecords);
    }

    /**
     * Mettre à jour un dossier médical existant
     * @param firstName Prénom de l'utilisateur
     * @param lastName Nom de famille de l'utilisateur
     * @param updatedMedicalRecord Les nouvelles informations du dossier médical
     * @return true si la mise à jour est réussie, false sinon
     */
    public boolean updateMedicalRecord(String firstName, String lastName, MedicalRecord updatedMedicalRecord) {
        List<MedicalRecord> medicalrecords= jsonFileWriter.getMedicalrecords();
        Optional<MedicalRecord> existingRecord = medicalrecords.stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(firstName)
                        && record.getLastName().equalsIgnoreCase(lastName))
                .findFirst();

        if (existingRecord.isPresent()) {
            int index = medicalrecords.indexOf(existingRecord.get());
            medicalrecords.set(index, updatedMedicalRecord);
            jsonFileWriter.setMedicalrecords(medicalrecords);
            return true;
        }
        return false;
    }

    /**
     * Supprimer un dossier médical
     * @param firstName Prénom de l'utilisateur
     * @param lastName Nom de famille de l'utilisateur
     * @return true si la suppression est réussie, false sinon
     */
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> medicalrecords= jsonFileWriter.getMedicalrecords();
        boolean resultat = medicalrecords.removeIf(record ->
                record.getFirstName().equalsIgnoreCase(firstName)
                        && record.getLastName().equalsIgnoreCase(lastName));
        jsonFileWriter.setMedicalrecords(medicalrecords);
        return resultat;
    }

    /**
     * Récupérer la liste de tous les dossiers médicaux.
     *
     * @return Liste des dossiers médicaux.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return jsonFileWriter.getMedicalrecords();
    }
}

