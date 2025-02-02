package com.safetynet.safetynetalerts.dto;

import java.util.List;

public class FireAlertDTO {

    private String fireStationNumber; // Numéro de la caserne de pompiers
    private List<ResidentInfo> residents; // Liste des résidents vivant à l'adresse

    public FireAlertDTO(String fireStationNumber, List<ResidentInfo> residents) {
        this.fireStationNumber = fireStationNumber;
        this.residents = residents;
    }

    public String getFireStationNumber() {
        return fireStationNumber;
    }

    public void setFireStationNumber(String fireStationNumber) {
        this.fireStationNumber = fireStationNumber;
    }

    public List<ResidentInfo> getResidents() {
        return residents;
    }

    public void setResidents(List<ResidentInfo> residents) {
        this.residents = residents;
    }

    // Classe interne pour représenter les informations de chaque résident
    public static class ResidentInfo {
        private String firstName;       // Prénom
        private String lastName;        // Nom
        private String phone;           // Numéro de téléphone
        private int age;                // Âge
        private List<String> medications; // Médicaments
        private List<String> allergies;   // Allergies

        public ResidentInfo(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.age = age;
            this.medications = medications;
            this.allergies = allergies;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getMedications() {
            return medications;
        }

        public void setMedications(List<String> medications) {
            this.medications = medications;
        }

        public List<String> getAllergies() {
            return allergies;
        }

        public void setAllergies(List<String> allergies) {
            this.allergies = allergies;
        }
    }
}