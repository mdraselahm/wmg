package com.weather.service;

import com.weather.dto.*;
import com.weather.entity.WeatherMetric;
import com.weather.exception.WeatherMetricQueryException;
import com.weather.repository.WeatherMetricRepository;
import com.weather.util.RequestValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * weather metric data read from database
 */
@Service
public class WeatherMetricService {

    private final MetricsCollectorService metricsCollectorService;
    private final WeatherMetricRepository weatherDataRepository;

    @Autowired
    public WeatherMetricService(WeatherMetricRepository weatherDataRepository, MetricsCollectorService metricsCollectorService) {
        this.weatherDataRepository = weatherDataRepository;
        this.metricsCollectorService = metricsCollectorService;
    }

    /**
     * return all metrics of given list of sensors
     *
     * @param weatherDataRequestDto
     * @return
     */
    public List<WeatherDataResponseDto> getWeatherMetrics(WeatherDataRequestDto weatherDataRequestDto) {
        try {
            RequestValidatorUtils.validateWeatherDataRequestDto(weatherDataRequestDto);

            final List<WeatherMetric> weatherMetrics = retrieveMetricsData(weatherDataRequestDto);
            final Map<String, List<WeatherMetric>> metricMap = weatherMetrics.stream()
                    .collect(Collectors.groupingBy(WeatherMetric::getSensorId));
            final Statistics statistics = Statistics.getValue(weatherDataRequestDto.getStatistic());

            return metricMap.entrySet().stream()
                    .map(entry -> WeatherDataResponseDto.builder()
                            .sensorId(entry.getKey())
                            .metrics(aggregateToStatistics(entry.getValue(), statistics))
                            .build()).toList();
        } catch (Exception e) {
            throw new WeatherMetricQueryException("Error during retrieval of weather metrics", e);
        }
    }

    private List<WeatherMetric> retrieveMetricsData(WeatherDataRequestDto weatherDataRequestDto) {
        if(weatherDataRequestDto.getStartDate() == null || weatherDataRequestDto.getEndDate() == null) {
            return weatherDataRequestDto.getSensorIds()
                    .stream()
                    .map(this::getLatestWeatherMetricBySensorId)
                    .toList()
                    .stream().flatMap(List::stream)
                    .toList();

        }
        return weatherDataRepository.findBySensorIdIgnoreCaseInAndMetricNameIgnoreCaseInAndTimestampBetween(weatherDataRequestDto.getSensorIds(),
                weatherDataRequestDto.getMetrics(), weatherDataRequestDto.getStartDate(), weatherDataRequestDto.getEndDate());
    }

    private List<WeatherMetric> getLatestWeatherMetricBySensorId(String sensorId) {
        final MetricsDto metricsDto = metricsCollectorService.getWeatherMetricsBySensorId(sensorId);
        return metricsCollectorService.saveWeatherMetrics(metricsDto);
    }

    private Map<String, Double> aggregateToStatistics(List<WeatherMetric> metrics, Statistics statistics) {
        Map<String, List<Double>> groupByStatistics =  metrics.stream()
                .collect(Collectors.groupingBy(WeatherMetric::getMetricName,
                        Collectors.mapping(WeatherMetric::getMetricValue, Collectors.toList())));

        return convertToStatistics(groupByStatistics, statistics);
    }

    private Map<String, Double> convertToStatistics(Map<String, List<Double>> groupByStatistics, Statistics statistics) {
        final Map<String, Double> metricsStatMap = new HashMap<>();
        for (Map.Entry<String, List<Double>> entry : groupByStatistics.entrySet()) {
            if (Statistics.MIN.equals(statistics)) {
                metricsStatMap.put(entry.getKey(), entry.getValue().stream().min(Double::compare).orElse(0.0));
            } else if (Statistics.MAX.equals(statistics)) {
                metricsStatMap.put(entry.getKey(), entry.getValue().stream().max(Double::compare).orElse(0.0));
            } else if (Statistics.SUM.equals(statistics)) {
                metricsStatMap.put(entry.getKey(), entry.getValue().stream().mapToDouble(Double::doubleValue).sum());
            } else {
                metricsStatMap.put(entry.getKey(), entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0));
            }
        }
        return  metricsStatMap;
    }
}
