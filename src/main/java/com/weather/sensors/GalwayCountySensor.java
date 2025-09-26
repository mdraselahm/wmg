package com.weather.sensors;

import com.weather.dto.MetricsDto;
import com.weather.dto.Sensor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Lazy
@Component
public class GalwayCountySensor implements ISensor {
    @Override
    public String getSensorId() {
        return Sensor.GALWAY_SENSOR.getSensorId();
    }

    @Override
    public MetricsDto readWeatherMetric(LocalDateTime localDateTime) {
        return MetricsDto.builder()
                .sensorId(getSensorId())
                .metrics(generateRandomMetrics())
                .timeStamp(localDateTime != null ? localDateTime : LocalDateTime.now())
                .build();
    }
}
