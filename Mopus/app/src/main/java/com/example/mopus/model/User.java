package com.example.mopus.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String id, firstName, lastName, email, phone, birthDay;
    private double height, weight;
    private boolean isProfessional, hasMenstrualCycle;

    public User() {

    }

    public User(String id, String firstName, String lastName, String email, String phone, String birthDay, boolean isProfessional) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDay = birthDay;
        this.isProfessional = isProfessional;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isProfessional() {
        return isProfessional;
    }

    public void setProfessional(boolean professional) {
        isProfessional = professional;
    }

    public boolean isHasMenstrualCycle() {
        return hasMenstrualCycle;
    }

    public void setHasMenstrualCycle(boolean hasMenstrualCycle) {
        this.hasMenstrualCycle = hasMenstrualCycle;
    }

    public Map<String, Object> toDB() {
        Map<String, Object> user = new HashMap<>();

        user.put("id", id);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);
        user.put("phone", phone);
        user.put("birthDay", birthDay);
        user.put("isProfessional", isProfessional);

        return user;
    }

    public Map<String, Object> IMC_toDB() {
        Map<String, Object> user = new HashMap<>();
        user.put("weight", weight);
        user.put("height", height);

        return user;
    }
}
