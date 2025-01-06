package com.booking.integrations.booking.service.model;

import com.booking.shared.Interval;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record BookingCommand(
        Long roomId, int noOfPersons, Interval interval
) {}
