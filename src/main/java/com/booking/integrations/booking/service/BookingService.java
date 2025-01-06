package com.booking.integrations.booking.service;

import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.integrations.booking.service.model.FindBookingCommand;
import com.booking.integrations.booking.service.exception.AllRoomsAreBookedException;
import com.booking.integrations.booking.service.model.Booking;

import java.util.List;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public interface BookingService {

    List<Booking> findBookings(FindBookingCommand findBookingCommand) throws AllRoomsAreBookedException;

    void saveBooking(BookingCommand bookingCommand);
}
