package com.room.booking.exception;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class MaintenanceTimingException extends  RuntimeException{
    private  String message;

    public MaintenanceTimingException() {}

    public MaintenanceTimingException(String msg)
    {
        super(msg);
        this.message = msg;
    }

}
