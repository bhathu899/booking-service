package com.booking.service.model;

import lombok.Builder;

import java.time.LocalTime;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record BookingDto(
        String roomName, int noOfPersons, LocalTime startTime, LocalTime endTime
) {}
