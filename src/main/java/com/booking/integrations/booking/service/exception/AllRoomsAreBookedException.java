package com.booking.integrations.booking.service.exception;


/**
 * Created by KrishnaKo on 20/01/2024
 */
public class AllRoomsAreBookedException extends Exception {
    public AllRoomsAreBookedException(String message) {
        super(message);
    }
}
