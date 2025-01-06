package com.booking.service;

import com.booking.service.config.MaintenanceTimeConfig;
import com.booking.integrations.booking.impl.persistence.repository.BookingRepository;
import com.booking.integrations.booking.impl.persistence.repository.RoomRepository;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.exception.AllRoomsAreBookedException;
import com.booking.service.impl.BookingManagerImpl;
import com.booking.integrations.booking.service.model.Booking;
import com.booking.service.model.BookingRequest;
import com.booking.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by KrishnaKo on 20/01/2024
 */

@ExtendWith(MockitoExtension.class)
class BookingManagerImplTest {

    @InjectMocks BookingManagerImpl bookingService;

    @Mock RoomRepository roomRepository;

    @Mock BookingRepository roomBookingRepository;

    @Mock MaintenanceTimeConfig maintenanceTimeConfig;

    @Test
    void testFetchAvailableRooms() {
        List<Booking> bookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(bookings);
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        List<Room> rooms = bookingService.fetchAvailableRooms(LocalTime.parse("08:00"), LocalTime.parse("09:00"));
        assertFalse(rooms.isEmpty());
        assertEquals(4, rooms.size());

    }

    @Test
    void testFetchAvailableRoomsWithFewOccupied() {
        List<Booking> bookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        List<Room> rooms = bookingService.fetchAvailableRooms(LocalTime.parse("08:00"), LocalTime.parse("09:00"));
        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());
        assertEquals(BEAUTY.getName(), rooms.get(0)
                                            .getRoomName());

    }

    @Test
    void testFetchAvailableRoomsMaintenanceTimeOverlapping() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        MaintenanceTimingException maintenanceTimingException = assertThrows(MaintenanceTimingException.class,
                                                                             () -> bookingService.fetchAvailableRooms(
                                                                                     LocalTime.parse("08:30"), LocalTime.parse("09:30")));
        assertNotNull(maintenanceTimingException);
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(), maintenanceTimingException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException = assertThrows(InvalidBookingTimingsException.class,
                                                                                     () -> bookingService.fetchAvailableRooms(
                                                                                             LocalTime.parse("24:30"),
                                                                                             LocalTime.parse("09:30")));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingEndTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException = assertThrows(InvalidBookingTimingsException.class,
                                                                                     () -> bookingService.fetchAvailableRooms(
                                                                                             LocalTime.parse("08:30"),
                                                                                             LocalTime.parse("09:90")));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testReserveRoomWithMaintenanceTimings() {

        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        MaintenanceTimingException maintenanceTimingException = assertThrows(MaintenanceTimingException.class,
                                                                             () -> bookingService.reserveRoom(
                                                                                     TestUtils.fetchBookingRoomRequestWithMaintenanceTimings()));
        assertNotNull(maintenanceTimingException);
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(), maintenanceTimingException.getMessage());
    }

    @Test
    void testReserveRoomWithIncorrectTimings() {
        InvalidBookingTimingsException invalidBookingTimingsException = assertThrows(InvalidBookingTimingsException.class,
                                                                                     () -> bookingService.reserveRoom(
                                                                                             TestUtils.fetchBookingRoomRequestWithInvalidTimings()));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testReserveRoomWithNoRoomsAvailableException() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.allRoomsBooked());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        AllRoomsAreBookedException allRoomsAreBookedException = assertThrows(AllRoomsAreBookedException.class,
                                                                             () -> bookingService.reserveRoom(
                                                                                   TestUtils.fetchBookingRoomRequestPayload()));
        assertNotNull(allRoomsAreBookedException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING.getMessage(), allRoomsAreBookedException.getMessage());

    }

    @Test
    void testReserveRoomWithNoRoomAvailableForPersons() {
        BookingRequest bookingRoomRequest = TestUtils.fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(8);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        AllRoomsAreBookedException allRoomsAreBookedException = assertThrows(AllRoomsAreBookedException.class,
                                                                             () -> bookingService.reserveRoom(bookingRoomRequest));
        assertNotNull(allRoomsAreBookedException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING_AND_PERSON.getMessage(), allRoomsAreBookedException.getMessage());

    }

    @Test
    void testReserveRoomWithOneRoomAvailable() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        Room room = bookingService.reserveRoom(TestUtils.fetchBookingRoomRequestPayload());
        assertNotNull(room);
        assertEquals(BEAUTY.getName(), room.getRoomName());
    }

    @Test
    void testReserveRoomWithTwoRoomsAvailable() {
        BookingRequest bookingRoomRequest = TestUtils.fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(3);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(
                TestUtils.fetchRoomBookingsTwoAvailable());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        Room room = bookingService.reserveRoom(bookingRoomRequest);
        assertNotNull(room);
        assertEquals(AMAZE.getName(), room.getRoomName());
    }

}

