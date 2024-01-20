package com.conferenceroom.booking.validator;

import com.conferenceroom.booking.exception.InvalidBookingTimingsException;

import static com.conferenceroom.booking.exception.ErrorCodesEnum.INVALID_BOOKING_TIMINGS;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class ConferenceRoomBookingTimeValidator {
    public static final String REGEX_TIME = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    public static void validateBookingTimings(String startTime, String endTime) {

        if (startTime.isBlank() || endTime.isBlank() || !startTime.matches(REGEX_TIME) ||
                !endTime.matches(REGEX_TIME)) {
            throw new InvalidBookingTimingsException(INVALID_BOOKING_TIMINGS.getMessage());
        }
    }
}
