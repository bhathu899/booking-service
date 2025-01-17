package com.booking.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;


@ConfigurationProperties(prefix = "maintenance")
@Configuration
@Data
public class MaintenanceTimeConfig {
    private List<TimeInterval> timings;

    @Data
    public static class TimeInterval {
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime startTime;
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime endTime;
    }

}
