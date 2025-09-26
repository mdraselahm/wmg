package com.weather.dto;

public enum Metrics {
    TEMPERATURE("temperature"),
    HUMIDITY("humidity");

    private String metric;

    Metrics(String metric) {
        this.metric = metric;
    }

    public String getMetric() {
        return this.metric;
    }

    public static Metrics getValue(String metric) {
        for (Metrics metrics:values()) {
            if(metrics.getMetric().equalsIgnoreCase(metric)) {
                return metrics;
            }
        }
        return null;
    }
}
