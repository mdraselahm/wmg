package com.weather.util;

import com.weather.exception.UnprocessableEntityException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * this class should be more test cases.
 * Written all test cases here only for RequestValidatorUtils.validateMetrics()
 * similar test case should be written for other validator methods also
 */
public class RequestValidatorUtilsTest {


    @Test
    public void validateMetrics_throwException_whenMetricsIsNull() {
        Throwable exception = assertThrows(UnprocessableEntityException.class, () -> RequestValidatorUtils.validateMetrics(null));
        assertEquals("Metrics is null","Metrics can't be empty", exception.getMessage());
    }

    @Test
    public void validateMetrics_throwException_whenMetricsIsEmpty() {
        Throwable exception = assertThrows(UnprocessableEntityException.class, () -> RequestValidatorUtils.validateMetrics(Collections.emptyList()));
        assertEquals("Metrics is empty","Metrics can't be empty", exception.getMessage());
    }

    @Test
    public void validateMetrics_throwException_whenInvalidMetricName() {
        Throwable exception = assertThrows(UnprocessableEntityException.class, () ->
                RequestValidatorUtils.validateMetrics(List.of("temperature", "humiXXdity")));
        assertEquals("Invalid metrics id","Invalid metrics id : humiXXdity", exception.getMessage());
    }

    @Test
    public void validateMetrics_shouldNotThrowException_whenAllValidMetrics() {
       assertTrue("temperature and humidity are valid metrics",
               RequestValidatorUtils.validateMetrics(List.of("temperature", "humidity")));
    }

    @Test
    public void validateMetrics_shouldNotThrowException_whenCaseInsensitiveMetrics() {
        assertTrue("temperature and humidity are valid metrics",
                RequestValidatorUtils.validateMetrics(List.of("tempeRAture", "HUMIDITY")));
    }
}
