package com.booking.persistence.service;

import com.booking.shared.model.Interval;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record BookingCommand(
        Long roomId, int noOfPersons, Interval interval
) {}
