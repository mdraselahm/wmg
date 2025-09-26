package com.weather.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class MetricsDto {
    private String sensorId;
    private Map<String, Double> metrics;
    private LocalDateTime timeStamp;
}
