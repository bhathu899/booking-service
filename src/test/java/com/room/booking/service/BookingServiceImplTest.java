package com.room.booking.service;

import com.room.booking.config.MaintenanceTimeConfig;
import com.room.booking.entity.Room;
import com.room.booking.entity.RoomBooking;
import com.room.booking.exception.InvalidBookingTimingsException;
import com.room.booking.exception.MaintenanceTimingException;
import com.room.booking.exception.NoRoomsAvailableException;
import com.room.booking.model.BookingRoomRequest;
import com.room.booking.repository.RoomBookingRepository;
import com.room.booking.repository.RoomRepository;
import com.room.booking.service.impl.BookingServiceImpl;
import com.room.booking.validator.BookingTimeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.room.booking.enums.RoomBookingEnum.AMAZE;
import static com.room.booking.enums.RoomBookingEnum.BEAUTY;
import static com.room.booking.exception.ErrorCodesEnum.INVALID_BOOKING_TIMINGS;
import static com.room.booking.exception.ErrorCodesEnum.MAINTENANCE_TIMINGS_ERROR;
import static com.room.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING;
import static com.room.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING_AND_PERSON;
import static com.room.booking.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by KrishnaKo on 20/01/2024
 */
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomBookingRepository roomBookingRepository;

    @Mock
    MaintenanceTimeConfig maintenanceTimeConfig;

    @Test
    void testFetchAvailableRooms() {
        List<RoomBooking> roomBookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(roomBookings);
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
        List<Room> roomList = bookingService.fetchAvailableRooms("08:00", "09:00");
        assertFalse(roomList.isEmpty());
        assertEquals(4, roomList.size());

    }

    @Test
    void testFetchAvailableRoomsWithFewOccupied() {
        List<RoomBooking> roomBookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
        List<Room> roomList = bookingService.fetchAvailableRooms("08:00", "09:00");
        assertFalse(roomList.isEmpty());
        assertEquals(1, roomList.size());
        assertEquals(BEAUTY.getName(), roomList.get(0).getRoomName());

    }

    @Test
    void testFetchAvailableRoomsMaintenanceTimeOverlapping() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        MaintenanceTimingException maintenanceTimingException =
                assertThrows(MaintenanceTimingException.class, () -> bookingService.fetchAvailableRooms("08:30", "09:30"));
        assertNotNull(maintenanceTimingException);
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(),maintenanceTimingException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.fetchAvailableRooms("24:30", "09:30"));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(),invalidBookingTimingsException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingEndTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.fetchAvailableRooms("08:30", "09:90"));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(),invalidBookingTimingsException.getMessage());
    }

    @Test
    void testReserveRoomWithMaintenanceTimings(){

        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        MaintenanceTimingException maintenanceTimingException =
                assertThrows(MaintenanceTimingException.class, () ->  bookingService.reserveRoom(fetchBookingRoomRequestWithMaintenanceTimings()));
        assertNotNull(maintenanceTimingException);
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(),maintenanceTimingException.getMessage());
    }
    @Test
    void testReserveRoomWithIncorrectTimings(){
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.reserveRoom(fetchBookingRoomRequestWithInvalidTimings()));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(),invalidBookingTimingsException.getMessage());
        }
    @Test
    void testReserveRoomWithNoRoomsAvailableException(){
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
    when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(),any())).thenReturn(allRoomsBooked());
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
        NoRoomsAvailableException noRoomsAvailableException =
                assertThrows(NoRoomsAvailableException.class, () -> bookingService.reserveRoom(fetchBookingRoomRequestPayload()));
        assertNotNull(noRoomsAvailableException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING.getMessage(),noRoomsAvailableException.getMessage());

     }

    @Test
    void testReserveRoomWithNoRoomAvailableForPersons(){
        BookingRoomRequest bookingRoomRequest = fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(8);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(),any())).thenReturn(fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
        NoRoomsAvailableException noRoomsAvailableException =
                assertThrows(NoRoomsAvailableException.class, () -> bookingService.reserveRoom(bookingRoomRequest));
        assertNotNull(noRoomsAvailableException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING_AND_PERSON.getMessage(),noRoomsAvailableException.getMessage());

    }
    @Test
    void testReserveRoomWithOneRoomAvailable(){
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(),any())).thenReturn(fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
         Room room = bookingService.reserveRoom(fetchBookingRoomRequestPayload());
        assertNotNull(room);
        assertEquals(BEAUTY.getName(),room.getRoomName());
    }

    @Test
    void testReserveRoomWithTwoRoomsAvailable(){
        BookingRoomRequest bookingRoomRequest = fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(3);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(),any())).thenReturn(fetchRoomBookingsTwoAvailable());
        when(roomRepository.findAll()).thenReturn(fetchAllRooms());
        Room room = bookingService.reserveRoom(bookingRoomRequest);
        assertNotNull(room);
        assertEquals(AMAZE.getName(),room.getRoomName());
    }

}
