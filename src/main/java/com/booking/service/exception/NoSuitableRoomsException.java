package com.booking.service.exception;


public class NoSuitableRoomsException extends Exception {
    public NoSuitableRoomsException(String message) {
        super(message);
    }

    public NoSuitableRoomsException(Throwable cause) {
        super(cause);
    }
}
