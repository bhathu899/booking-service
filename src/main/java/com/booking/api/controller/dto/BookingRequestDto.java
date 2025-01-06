package com.booking.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalTime;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Builder
public record BookingRequestDto(
        @JsonProperty("startTime") @FutureOrPresent @NotNull LocalTime startTime,
        @JsonProperty("endTime") @FutureOrPresent @NotNull LocalTime endTime,
        @JsonProperty("noOfPersons") @Size(min = 2, max = 20) int noOfPersons
) {

}
