package com.booking.api.controller.dto;

import com.booking.validator.ValidBookingTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@ValidBookingTime
public record BookingRequestDto(
        @JsonProperty("startTime") @NotNull  LocalTime startTime,
        @JsonProperty("endTime")  @NotNull LocalTime endTime,
        @JsonProperty("noOfPersons") @Min(value = 2,message = "Min persons must be greater than or equal to 2"  ) @Max(value = 20,message =
                "Max persons must be less than or equal to 20") int noOfPersons
) {

}
