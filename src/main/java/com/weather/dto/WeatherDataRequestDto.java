package com.weather.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDataRequestDto {
    private List<String> sensorIds;
    private List<String> metrics;
    private String statistic;       // min, max, sum, avg
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
