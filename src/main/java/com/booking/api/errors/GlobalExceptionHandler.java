package com.booking.api.errors;

import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler{


        @ExceptionHandler(value = Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public @ResponseBody ErrorDto handleException(Exception ex) {
            return new ErrorDto("BKS-04", ex.getMessage());
        }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()));
        return new ErrorDto("BKS-VALIDATE", errorMessages.toString());
    }
}
