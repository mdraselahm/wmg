package com.weather.exception;

public class WeatherMetricQueryException extends RuntimeException {
    public WeatherMetricQueryException(String message, Throwable t) {
        super(message, t);
    }
}
