package com.example.mopus.model;

import java.util.HashMap;
import java.util.Map;

public class WaterStats {
    private HashMap<String, Integer> day_total = new HashMap<String, Integer>();
    private HashMap<String, Integer> hour_water = new HashMap<String, Integer>();

    public WaterStats() {

    }

    public Map<String, Object> toDB() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("day_total", day_total);
        stats.put("hour_water", hour_water);

        return stats;
    }
}
