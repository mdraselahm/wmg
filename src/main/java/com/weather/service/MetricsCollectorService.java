package com.weather.service;

import com.weather.dto.MetricsDto;
import com.weather.entity.WeatherMetric;
import com.weather.repository.WeatherMetricRepository;
import com.weather.sensors.ISensor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * this class is dummy sensor data reader.
 * here we are simple generating some dummy weather data
 */
@Service
public class MetricsCollectorService {

    private final WeatherMetricRepository weatherMetricRepository;
    private final Map<String, ISensor> sensors;

    @Autowired
    public MetricsCollectorService(List<ISensor> sensors, WeatherMetricRepository weatherMetricRepository) {
        this.sensors = toMap(sensors, ISensor::getSensorId);
        this.weatherMetricRepository = weatherMetricRepository;
    }

    @PostConstruct
    public void generateDummyMetrics() {
        for(ISensor sensor: sensors.values()) {
            for(int j = 0; j <= 30; j++) {
                saveWeatherMetrics(sensor.readWeatherMetric(LocalDateTime.now().minusDays((long)j)));
            }
        }
    }

    public MetricsDto getWeatherMetricsBySensorId(final String sensorId) {
        return sensors.get(sensorId).readWeatherMetric(null);
    }

    /**
     * save weather metrics to db
     *
     * @param metricsDto
     */
    public List<WeatherMetric> saveWeatherMetrics(MetricsDto metricsDto) {
        if(metricsDto != null) {
            final String sensorId = metricsDto.getSensorId();
            final LocalDateTime timeStamp = metricsDto.getTimeStamp();
            final List<WeatherMetric> weatherMetric = metricsDto.getMetrics()
                    .entrySet()
                    .stream()
                    .map(m -> createWeatherMetricsEntity(sensorId, m.getKey(), m.getValue(), timeStamp))
                    .toList();

            return weatherMetricRepository.saveAll(weatherMetric);
        }
        return null;
    }

    private <V> Map<String, V> toMap(List<V> source, Function<V, String> keyMapper) {
        return source.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    private WeatherMetric createWeatherMetricsEntity(String sensorId, String metricName, Double metricValue, LocalDateTime timestamp) {
        return WeatherMetric.builder()
                .sensorId(sensorId)
                .metricName(metricName)
                .metricValue(metricValue)
                .timestamp(timestamp)
                .build();
    }
}