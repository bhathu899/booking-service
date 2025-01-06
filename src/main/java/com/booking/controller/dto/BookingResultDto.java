package com.booking.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record BookingResultDto(@JsonProperty("roomName") String name, @JsonProperty("roomSize") int size) {}
