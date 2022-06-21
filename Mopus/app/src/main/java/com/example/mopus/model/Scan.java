package com.example.mopus.model;

public class Scan {
    private String userEmail;
    private String dateTime;
    private String timestamp;

    public Scan(String timestamp, String userEmail, String dateTime) {
        this.timestamp = timestamp;
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
