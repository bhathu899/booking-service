package com.booking.service;

import com.booking.integrations.booking.impl.BookingServiceImpl;
import com.booking.integrations.booking.impl.persistence.entity.BookingEntity;
import com.booking.integrations.booking.impl.persistence.repository.BookingRepository;
import com.booking.integrations.booking.service.model.Booking;
import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.shared.Interval;
import com.booking.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks BookingServiceImpl bookingService;

    @Mock BookingRepository bookingRepository;

    @Test
    void testFindBookingWithAllRoomsAreBooked() {
        when(bookingRepository.findBookings(any(), any())).thenReturn(TestUtils.allRoomsBooked());
        List<Booking> bookings = bookingService.findBookings(TestUtils.fetchFindBookingCommand());
        assertNotNull(bookings);
        assertEquals(4, bookings.size());

    }

    @Test
    void testFindBookingWithNoRoomsBooked() {
        when(bookingRepository.findBookings(any(), any())).thenReturn(new ArrayList<>());
        List<Booking> bookings = bookingService.findBookings(TestUtils.fetchFindBookingCommand());
        assertNotNull(bookings);
        assertEquals(0, bookings.size());

    }

    @Test
    void testSaveBooking() {
        when(bookingRepository.save(any())).thenReturn(new BookingEntity());
        bookingService.saveBooking(BookingCommand.builder()
                                                 .interval(new Interval(LocalTime.of(10, 0), LocalTime.of(11, 0)))
                                                 .noOfPersons(3)
                                                 .roomId(1L)
                                                 .build());

        verify(bookingRepository).save(any());

    }
}
