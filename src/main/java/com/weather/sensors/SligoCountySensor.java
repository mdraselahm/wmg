package com.weather.sensors;

import com.weather.dto.MetricsDto;
import com.weather.dto.Sensor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Lazy
@Component
public class SligoCountySensor implements ISensor{
    @Override
    public String getSensorId() {
        return Sensor.SLIGO_SENSOR.getSensorId();
    }

    /**
     * static dummy metric redaing as sensor data raeder
     * real case, we will incorporate some API/port reader/listen streaming topic..something like this
     *
     * @param localDateTime
     * @return
     */
    @Override
    public MetricsDto readWeatherMetric(LocalDateTime localDateTime) {
        return MetricsDto.builder()
                .sensorId(getSensorId())
                .metrics(generateRandomMetrics())
                .timeStamp(localDateTime != null ? localDateTime : LocalDateTime.now())
                .build();
    }
}
