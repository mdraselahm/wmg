package com.weather.sensors;

import com.weather.dto.Metrics;
import com.weather.dto.MetricsDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public interface ISensor {
    Random random = new Random();
    /**
     * return sensor unique id
     *
     * @return
     */
     String getSensorId();

    /**
     * read weather metric from another server
     *
     * @return
     */
    MetricsDto readWeatherMetric(LocalDateTime localDateTime);

    default Map<String, Double> generateRandomMetrics() {
        return Map.of(
            Metrics.TEMPERATURE.getMetric(), Math.round(-10.0 + (55.0 * random.nextDouble()) * 100.0) / 100.0,
            Metrics.HUMIDITY.getMetric(), Math.round(10.0 + (90.0 * random.nextDouble()) * 100.0) / 100.0
        );
    }
}
