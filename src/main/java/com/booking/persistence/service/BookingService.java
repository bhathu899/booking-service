package com.booking.persistence.service;

import com.booking.persistence.BookingCommand;
import com.booking.persistence.FindBookingCommand;
import com.booking.service.exception.NoRoomsAvailableException;
import com.booking.service.model.Booking;

import java.util.List;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public interface BookingService {

    List<Booking> findBookings(FindBookingCommand findBookingCommand) throws NoRoomsAvailableException;

    void saveBooking(BookingCommand bookingCommand);
}
