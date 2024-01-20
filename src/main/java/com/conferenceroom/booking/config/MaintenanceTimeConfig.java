package com.conferenceroom.booking.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@ConfigurationProperties(prefix = "maintenance")
@Configuration
@Getter
@Setter
public class MaintenanceTimeConfig {
    List<String> timings;
}
