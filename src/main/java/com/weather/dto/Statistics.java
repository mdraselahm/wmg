package com.weather.dto;

public enum Statistics {
    MIN("min"),
    MAX("max"),
    SUM("sum"),
    AVERAGE("avg");

    private String label;

    Statistics(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static Statistics getValue(String stat) {
        for(Statistics statistics:Statistics.values()) {
            if(statistics.getLabel().equalsIgnoreCase(stat)) {
                return statistics;
            }
        }
        return Statistics.AVERAGE;
    }
}
