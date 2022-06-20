package com.example.mopus.model;

public class Scan {
    private String userEmail;
    private String dateTime;

    public Scan(String userEmail, String dateTime) {
        this.userEmail = userEmail;
        this.dateTime = dateTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getDateTime() {
        return dateTime;
    }
}
