package com.safetynet.safetynetalerts.dto;

import java.util.List;
import java.util.Map;

public class FloodStationsDTO {

    private Map<String, List<HouseholdInfo>> householdsByAddress; // Adresses et leurs résidents

    public FloodStationsDTO(Map<String, List<HouseholdInfo>> householdsByAddress) {
        this.householdsByAddress = householdsByAddress;
    }

    public Map<String, List<HouseholdInfo>> getHouseholdsByAddress() {
        return householdsByAddress;
    }

    public void setHouseholdsByAddress(Map<String, List<HouseholdInfo>> householdsByAddress) {
        this.householdsByAddress = householdsByAddress;
    }

    // Classe interne pour représenter un résident d'un foyer
    public static class HouseholdInfo {
        private String firstName;        // Prénom
        private String lastName;         // Nom
        private String phone;            // Numéro de téléphone
        private int age;                 // Âge
        private List<String> medications; // Médicaments
        private List<String> allergies;   // Allergies

        public HouseholdInfo(String firstName, String lastName, String phone, int age, List<String> medications, List<String> allergies) {
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