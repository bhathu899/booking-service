package com.conferenceroom.booking.exception;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class InvalidBookingTimingsException extends  RuntimeException {
    private String message;

    public InvalidBookingTimingsException(String msg) {
        super(msg);
        this.message = msg;
    }

}
