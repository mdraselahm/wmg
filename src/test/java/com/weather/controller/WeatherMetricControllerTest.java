package com.weather.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.dto.WeatherDataResponseDto;
import com.weather.entity.WeatherMetric;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.AssertionErrors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherMetricControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testPostWeatherMetricRequest() throws Exception {
        String requestJSON = """
            {
              "sensorIds": ["dublin-sensor","galway-sensor", "sligo-sensor"],
              "metrics": ["temperAture" , "HUMIDITY"],
              "statistic": "avg",
              "startDate": "2025-09-18T00:00:00",
              "endDate": "2025-09-25T00:00:00"
            }
        """;
        MvcResult result = mockMvc.perform(post("/api/weather/metrics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        List<WeatherDataResponseDto> response = objectMapper.readValue(responseBody, new TypeReference<List<WeatherDataResponseDto>>() {});
        final Map<String, List<WeatherDataResponseDto>> metricMap = response.stream()
                .collect(Collectors.groupingBy(WeatherDataResponseDto::getSensorId));

        assertEquals("Error on api result size mismatch", 3, response.size());
    }
}
