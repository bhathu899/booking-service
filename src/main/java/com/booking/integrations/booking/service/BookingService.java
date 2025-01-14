package com.booking.integrations.booking.service;

import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.integrations.booking.service.model.FindBookingCommand;
import com.booking.integrations.booking.service.model.Booking;

import java.util.List;


public interface BookingService {

    List<Booking> findBookings(FindBookingCommand findBookingCommand);

    void saveBooking(BookingCommand bookingCommand);
}
