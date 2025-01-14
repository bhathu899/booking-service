package com.booking.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record BookingResultDto(@JsonProperty("roomName") String name, @JsonProperty("roomSize") int size) {}
