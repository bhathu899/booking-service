package com.booking.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public record BookingResultDto(@JsonProperty("roomName") String name, @JsonProperty("roomSize") int size) {}
