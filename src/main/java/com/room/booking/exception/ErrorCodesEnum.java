package com.room.booking.exception;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public enum ErrorCodesEnum {

    MAINTENANCE_TIMINGS_ERROR("Maintenance is under progress, For Given Timings"),
    INVALID_BOOKING_TIMINGS("Given  Booking Times are invalid"),

    NO_ROOMS_AVAILABLE_TIMING("No Rooms Available For Given Timing"),
    NO_ROOMS_AVAILABLE_TIMING_AND_PERSON("No Rooms Available for Given Timing and No of Persons ")
    ;

    private final String message;
    ErrorCodesEnum(final String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
