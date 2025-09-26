package com.weather.dto;

public enum Sensor {
    DUBLIN_SENSOR("dublin-sensor"),
    GALWAY_SENSOR("galway-sensor"),
    SLIGO_SENSOR("sligo-sensor");

    private String sensorId;

    Sensor(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorId() {
        return this.sensorId;
    }

    public static Sensor getValue(String sensorId) {
        for (Sensor sensor:values()) {
            if(sensor.getSensorId().equalsIgnoreCase(sensorId)) {
                return sensor;
            }
        }
        return null;
    }
}
