package com.example.mopus.model;

import java.util.HashMap;
import java.util.Map;

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

    public String getTimestamp() { return timestamp; }

    public Map<String, Object> toDB() {
        Map<String, String> bdFormat = new HashMap<>();
        bdFormat.put("date", dateTime);
        bdFormat.put("user_email", userEmail);

        Map<String, Object> finalFormat = new HashMap<>();
        finalFormat.put(timestamp, bdFormat);

        return finalFormat;
    }
}
