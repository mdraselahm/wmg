package com.weather.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherMetric {
    @Id
    @GeneratedValue
    private Long id;
    private String sensorId;
    private String metricName;
    private Double metricValue;
    private LocalDateTime timestamp;
}

