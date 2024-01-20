package com.room.booking.validator;

import com.room.booking.exception.InvalidBookingTimingsException;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

import static com.room.booking.exception.ErrorCodesEnum.INVALID_BOOKING_TIMINGS;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class BookingTimeValidator {
    public static final String REGEX_TIME = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static void validateBookingTimings(String startTime, String endTime) {

        if (startTime.isBlank() || endTime.isBlank() || !startTime.matches(REGEX_TIME) ||
                !endTime.matches(REGEX_TIME)) {
            throw new InvalidBookingTimingsException(INVALID_BOOKING_TIMINGS.getMessage());
        }
    }
}
