package com.weather.controller;

import com.weather.dto.WeatherDataRequestDto;
import com.weather.dto.WeatherDataResponseDto;
import com.weather.service.WeatherMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherMetricController {

    @Autowired
    private WeatherMetricService weatherMetricService;

    @PostMapping("/metrics")
    public ResponseEntity<List<WeatherDataResponseDto>> getWeatherMetrics(@RequestBody WeatherDataRequestDto weatherDataRequestDto
    ) {
        return ResponseEntity.ok(weatherMetricService.getWeatherMetrics(weatherDataRequestDto));
    }
}
