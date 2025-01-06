package com.booking.persistence;

import com.booking.shared.model.Interval;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record BookingCommand(
        int roomId, int noOfPersons, Interval interval
) {}
