package com.booking.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@ConfigurationProperties(prefix = "maintenance")
@Configuration
@Data
public class MaintenanceTimeConfig {
    private List<TimeInterval> timings;

    @Data
    public static class TimeInterval {
        private LocalTime startTime;
        private LocalTime endTime;
    }

}
