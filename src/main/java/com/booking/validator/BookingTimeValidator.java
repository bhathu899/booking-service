package com.booking.validator;

import com.booking.api.controller.dto.BookingRequestDto;
import com.booking.shared.Interval;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;



public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, BookingRequestDto> {


    @Override
    public boolean isValid(BookingRequestDto request, ConstraintValidatorContext context) {
        LocalTime startTime = request.startTime();
        LocalTime endTime = request.endTime();

       if (startTime == null || endTime == null) {
            return false;
        }

        if (startTime.isAfter(endTime)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Booking start time should be before booking end time")
                   .addConstraintViolation();
            return false;
        }

        Duration duration = Duration.between(startTime.atDate(LocalDate.now()), endTime.atDate(LocalDate.now()));
        if (duration.toMinutes() % 15 != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Booking interval should be in terms of 15 minutes")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
