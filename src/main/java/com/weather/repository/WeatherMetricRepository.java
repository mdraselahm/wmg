package com.weather.repository;

import com.weather.entity.WeatherMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherMetricRepository extends JpaRepository<WeatherMetric, Long> {

    List<WeatherMetric> findBySensorIdIgnoreCaseInAndMetricNameIgnoreCaseInAndTimestampBetween(
            List<String> sensorIds, List<String> metrics, LocalDateTime from, LocalDateTime to
    );
}
