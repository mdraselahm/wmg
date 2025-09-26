package com.weather.util;

import com.weather.dto.Metrics;
import com.weather.dto.Sensor;
import com.weather.dto.WeatherDataRequestDto;
import com.weather.exception.UnprocessableEntityException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RequestValidatorUtils {

    public static void validateWeatherDataRequestDto(WeatherDataRequestDto weatherDataRequestDto) {
        validateSensorIds(weatherDataRequestDto.getSensorIds());
        validateMetrics(weatherDataRequestDto.getMetrics());
        validateDateRange(weatherDataRequestDto.getStartDate(), weatherDataRequestDto.getEndDate());
    }

    public static boolean validateMetrics(List<String> metrics) {
        if(metrics == null || metrics.isEmpty()) {
            throwUnprocessableEntityException("Metrics can't be empty");
        }
        metrics.forEach( m -> {
            if (m == null || Metrics.getValue(m) == null) {
                throwUnprocessableEntityException(String.format("Invalid metrics id : %s", m));
            }
        });
        return true;
    }

    public static boolean validateSensorIds(List<String> sensorIds) {
        if(sensorIds == null || sensorIds.isEmpty()) {
            throwUnprocessableEntityException("Sensor Ids can't be empty");
        }
        sensorIds.forEach( sensorId -> {
            if (sensorId == null || Sensor.getValue(sensorId) == null) {
                throwUnprocessableEntityException(String.format("Invalid sensor id : %s", sensorId));
            }
        });
        return true;
    }

    public static boolean validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate != null && endDate != null && !isDurationBetween1And30Days(startDate, endDate)) {
            throwUnprocessableEntityException(String.format("Date range is not in between 1 and 30 days. start date: %s , end date : %s", startDate, endDate));
        }
        return true;
    }

    public static boolean isDurationBetween1And30Days(LocalDateTime start, LocalDateTime end) {
        long daysBetween = ChronoUnit.DAYS.between(start, end);
        return daysBetween >= 1 && daysBetween <= 30;
    }

    private static void throwUnprocessableEntityException(String message) {
        throw new UnprocessableEntityException(message);
    }
}
