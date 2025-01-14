package com.booking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Constraint(validatedBy = BookingTimeValidator.class)
@Target({ElementType.TYPE ,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTime {

    String message() default "Invalid booking time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}