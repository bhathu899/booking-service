package com.conferenceroom.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by KrishnaKo on 20/01/2024
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value
            = NoRoomsAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse
    handleNoRoomsAvailableException(NoRoomsAvailableException ex)
    {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }



    @ExceptionHandler(value
            = MaintenanceTimingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse
    handleMaintenanceTimingException(MaintenanceTimingException ex)
    {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }




    @ExceptionHandler(value
            = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse
    handleMaintenanceTimingException(Exception ex)
    {
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
