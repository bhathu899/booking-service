package com.room.booking.exception;


/**
 * Created by KrishnaKo on 20/01/2024
 */
public class NoRoomsAvailableException extends  RuntimeException {

    private String message;

    public NoRoomsAvailableException(){
 }

    public NoRoomsAvailableException(String msg)
    {
        super(msg);
        this.message = msg;
    }

}
