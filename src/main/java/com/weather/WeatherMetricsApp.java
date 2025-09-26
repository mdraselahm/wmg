package com.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeatherMetricsApp {

    public static void main(String[] args) {
        SpringApplication.run(WeatherMetricsApp.class, args);
    }
}
