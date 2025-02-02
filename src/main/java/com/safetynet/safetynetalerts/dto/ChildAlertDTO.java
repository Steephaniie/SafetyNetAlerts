package com.safetynet.safetynetalerts.dto;

import java.util.List;

public class ChildAlertDTO {
    private List<ChildInfo> children; // Liste des enfants habitant à l'adresse
    private List<HouseholdMember> otherHouseholdMembers; // Liste des autres membres du foyer

    public ChildAlertDTO(List<ChildInfo> children, List<HouseholdMember> otherHouseholdMembers) {
        this.children = children;
        this.otherHouseholdMembers = otherHouseholdMembers;
    }

    public List<ChildInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ChildInfo> children) {
        this.children = children;
    }

    public List<HouseholdMember> getOtherHouseholdMembers() {
        return otherHouseholdMembers;
    }

    public void setOtherHouseholdMembers(List<HouseholdMember> otherHouseholdMembers) {
        this.otherHouseholdMembers = otherHouseholdMembers;
    }

    // Classe interne pour représenter les informations sur les enfants
    public static class ChildInfo {
        private String firstName;
        private String lastName;
        private int age;

        public ChildInfo(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    // Classe interne pour représenter les autres membres du foyer
    public static class HouseholdMember {
        private String firstName;
        private String lastName;

        public HouseholdMember(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
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
    }
}