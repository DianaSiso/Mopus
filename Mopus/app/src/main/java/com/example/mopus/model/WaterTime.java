package com.example.mopus.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class WaterTime {
    private int water_total;
    private int water_cup;
    private int sleep_time;
    private int wake_up_time;
    private int hours;
    private HashMap<String, Double> stats_per_day = new HashMap<>();
    private HashMap<String, Double> temp = new HashMap<>();
    private HashMap<String, HashMap<String, Double>> stats_per_hour = new HashMap<>();

    public WaterTime(int water_total, int water_cup, int sleep_time, int wake_up_time) {
        this.wake_up_time = wake_up_time;
        this.sleep_time = sleep_time;
        this.water_cup = water_cup;
        this.water_total = water_total;
        this.hours = sleep_time - wake_up_time;
    }

    public WaterTime(String day, double water) {
        this.stats_per_day.put(day, water);
    }

    public WaterTime(String day, String hour, double water) {
        this.temp.put(hour, water);
        this.stats_per_hour.put(day, this.temp);
    }

    public int getWakeUpTime() {
        return wake_up_time;
    }
    public void setWakeUpTime(int wake_up_time) {
        this.wake_up_time = wake_up_time;
    }

    public int getSleepTime() {
        return sleep_time;
    }
    public void setSleepTime(int sleep_time) {
        this.sleep_time = sleep_time;
    }

    public int getWaterTotal() {
        return water_total;
    }
    public void setWaterTotal(int water_total) {
        this.water_total = water_total;
    }

    public int getWaterCup() {
        return water_cup;
    }
    public void setWaterCup(int water_cup) {
        this.water_cup = water_cup;
    }

    public HashMap<String, Double> getStats_per_day() { return stats_per_day; }
    public void setStats_per_day(HashMap<String, Double> stats_per_day) { this.stats_per_day = stats_per_day; }

    public HashMap<String, HashMap<String, Double>> getStats_per_hour() { return stats_per_hour; }
    public void setStats_per_hour(HashMap<String, HashMap<String, Double>> stats_per_hour) { this.stats_per_hour = stats_per_hour; }

    public Map<String, Object> toDB() {
        Map<String, Object> drink = new HashMap<>();

        drink.put("wake_up_time", wake_up_time);
        drink.put("sleep_time", sleep_time);
        drink.put("water_cup", water_cup);
        drink.put("water_total", water_total);
        drink.put("active_hours", hours);

        return drink;
    }
    public Map<String, Object> toDB_per_day(HashMap<String, Double> stats) {
        Map<String, Object> drink = new HashMap<>();
        drink.put("stats_per_day", stats);
        return drink;
    }
    public Map<String, Object> toDB_per_hour(HashMap<String, HashMap<String, Double>> stats) {
        Map<String, Object> drink = new HashMap<>();
        drink.put("stats_per_hour", stats);
        return drink;
    }

}
