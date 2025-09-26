package com.weather.service;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.mockito.Mockito.*;

import com.weather.dto.MetricsDto;
import com.weather.dto.WeatherDataRequestDto;
import com.weather.dto.WeatherDataResponseDto;
import com.weather.entity.WeatherMetric;
import com.weather.repository.WeatherMetricRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class WeatherMetricServiceTest {

    @Mock
    private MetricsCollectorService metricsCollectorService;
    @Mock
    private WeatherMetricRepository weatherDataRepository;
    @InjectMocks
    private WeatherMetricService weatherMetricService;

    private static String SENSOR_D = "dublin-sensor";

    @Test
    public void getWeatherMetrics_ReturnAverageMetricsValue_WhenValidSensorAndMetricsAndDateRange() {
        WeatherDataRequestDto weatherDataRequestDto = requestDto("avg",
                LocalDateTime.parse("2025-09-18T00:00:00"),
                LocalDateTime.parse("2025-09-25T00:00:00"));
        when(weatherDataRepository.findBySensorIdIgnoreCaseInAndMetricNameIgnoreCaseInAndTimestampBetween(
                        weatherDataRequestDto.getSensorIds(),
                        weatherDataRequestDto.getMetrics(),
                        weatherDataRequestDto.getStartDate(),
                        weatherDataRequestDto.getEndDate()))
                .thenReturn(getWeatherMetricList());

        List<WeatherDataResponseDto> responseDtos = weatherMetricService.getWeatherMetrics(weatherDataRequestDto);


        assertEquals("Error on sensor size mismatch during ValidDateRange", 1, responseDtos.size());
        assertEquals("Error on metrics size mismatch during ValidDateRange", 2, responseDtos.get(0).getMetrics().size());
        assertEquals("Error on min temperature matching  during ValidDateRange", 25.0, responseDtos.get(0).getMetrics().get("temperature"));
        assertEquals("Error on min humidity matching  during ValidDateRange", 40.0, responseDtos.get(0).getMetrics().get("humidity"));

        verify(weatherDataRepository).findBySensorIdIgnoreCaseInAndMetricNameIgnoreCaseInAndTimestampBetween(
                eq(weatherDataRequestDto.getSensorIds()),
                eq(weatherDataRequestDto.getMetrics()),
                eq(weatherDataRequestDto.getStartDate()),
                eq(weatherDataRequestDto.getEndDate())
        );
    }

    @Test
    public void getWeatherMetrics_ReturnMinMetricsValue_WhenValidSensorAndMetricsAndInvalidDateRange() {
        WeatherDataRequestDto weatherDataRequestDto = requestDto("min",
                null,
                LocalDateTime.parse("2025-09-25T00:00:00"));
        final MetricsDto metricsDto = createMetricsDto();
        when(metricsCollectorService.getWeatherMetricsBySensorId(SENSOR_D)).thenReturn(metricsDto);
        when(metricsCollectorService.saveWeatherMetrics(metricsDto)).thenReturn(getWeatherMetricList());

        List<WeatherDataResponseDto> responseDtos = weatherMetricService.getWeatherMetrics(weatherDataRequestDto);

        assertEquals("Error on sensor size mismatch during InvalidDateRange", 1, responseDtos.size());
        assertEquals("Error on metrics size mismatch  during InvalidDateRange", 2, responseDtos.get(0).getMetrics().size());
        assertEquals("Error on min temperature matching  during InvalidDateRange", 16.0, responseDtos.get(0).getMetrics().get("temperature"));
        assertEquals("Error on min humidity matching  during InvalidDateRange", 30.0, responseDtos.get(0).getMetrics().get("humidity"));

        verify(metricsCollectorService).getWeatherMetricsBySensorId(SENSOR_D);
        verify(metricsCollectorService).saveWeatherMetrics(metricsDto);
    }

    private WeatherDataRequestDto requestDto(String statistics, LocalDateTime start, LocalDateTime end) {
        return WeatherDataRequestDto.builder()
                .sensorIds(List.of(SENSOR_D))
                .metrics(List.of("temperature", "humidity"))
                .statistic(statistics)
                .startDate(start)
                .endDate(end)
                .build();
    }

    private List<WeatherMetric> getWeatherMetricList() {
        return List.of(
                createWeatherMetric(SENSOR_D, "temperature", 34.0, "2025-09-21T00:00:00"),
                createWeatherMetric(SENSOR_D, "temperature", 16.0, "2025-09-23T00:00:00"),
                createWeatherMetric(SENSOR_D, "humidity", 50.0, "2025-09-22T00:00:00"),
                createWeatherMetric(SENSOR_D, "humidity", 30.0, "2025-09-18T00:00:00")
        );
    }

    private WeatherMetric createWeatherMetric(String sensorId, String metricName, Double metricValue, String dateTime) {
        return WeatherMetric.builder()
                .sensorId(sensorId)
                .metricName(metricName)
                .metricValue(metricValue)
                .timestamp(LocalDateTime.parse(dateTime))
                .build();
    }

    private MetricsDto createMetricsDto () {
       return MetricsDto.builder()
                .sensorId(SENSOR_D)
                .metrics(Map.of(
                "temperature", 34.0,
                "humidity", 30.0))
                .timeStamp(LocalDateTime.parse("2025-09-25T00:00:00"))
                .build();
    }
}
