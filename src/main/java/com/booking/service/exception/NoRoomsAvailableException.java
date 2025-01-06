package com.booking.service.exception;


/**
 * Created by KrishnaKo on 20/01/2024
 */
public class NoRoomsAvailableException extends Exception {
    public NoRoomsAvailableException(String message) {
        super(message);
    }
}
