package com.booking.service.exception;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class NoSuitableRoomsException extends Exception {
    public NoSuitableRoomsException(String message) {
        super(message);
    }

    public NoSuitableRoomsException(Throwable cause) {
        super(cause);
    }
}
