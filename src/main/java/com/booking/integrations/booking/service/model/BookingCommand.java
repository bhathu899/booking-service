package com.booking.integrations.booking.service.model;

import com.booking.shared.Interval;
import lombok.Builder;


@Builder
public record BookingCommand(
        Long roomId, int noOfPersons, Interval interval
) {}
