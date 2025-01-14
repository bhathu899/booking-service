package com.booking.service;

import com.booking.integrations.booking.service.BookingService;
import com.booking.integrations.booking.service.RoomService;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.config.MaintenanceTimeConfig;
import com.booking.service.config.MaintenanceTimeConfig.TimeInterval;
import com.booking.service.exception.InvalidTimeException;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoSuitableRoomsException;
import com.booking.service.impl.BookingManagerImpl;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;
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

import static com.booking.integrations.booking.service.model.RoomSearchResult.EmptyRoomReason.ALL_ROOMS_BOOKED;
import static com.booking.integrations.booking.service.model.RoomSearchResult.EmptyRoomReason.OVERLAP_WITH_MAINTENANCE_TIME;
import static com.booking.utils.TestUtils.AMAZE;
import static com.booking.utils.TestUtils.BEAUTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;




@ExtendWith(MockitoExtension.class)
class BookingManagerImplTest {


    @InjectMocks BookingManagerImpl bookingManager;

    @Mock MaintenanceTimeConfig maintenanceTimeConfig;

    @Mock BookingService bookingService;

    @Mock

    RoomService roomService;


    @Test
    void testFetchAvailableRooms() throws NoSuitableRoomsException, InvalidTimeException {
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(new ArrayList<>());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0)));
        List<Room> rooms = roomSearchResult.rooms();
        assertFalse(rooms.isEmpty());
        assertEquals(4, rooms.size());

    }

    private void setMaintenanceTime() {
        TimeInterval timeInterval = new TimeInterval();
        timeInterval.setStartTime(LocalTime.of(9, 0));
        timeInterval.setEndTime(LocalTime.of(9, 15));
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList(timeInterval));
    }

    private RoomSearchResult fetchAllRooms() {
        return new RoomSearchResult(TestUtils.fetchAllRooms(), null);
    }

    @Test
    void testFetchAvailableRoomsWithFewOccupied() throws NoSuitableRoomsException, InvalidTimeException {
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(TestUtils.fetchThreeRoomBookings());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(new Interval(LocalTime.of(8, 0), LocalTime.of(9, 0)));
        List<Room> rooms = roomSearchResult.rooms();
        assertFalse(rooms.isEmpty());
        assertEquals(1, rooms.size());
        assertEquals(BEAUTY.name(), rooms.get(0)
                                         .name());

    }

    @Test
    void testFetchAvailableRoomsNoRoomsCase() throws NoSuitableRoomsException, InvalidTimeException {
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(TestUtils.allBookings());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        setMaintenanceTime();
        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(new Interval(LocalTime.of(8, 30), LocalTime.of(8, 45)));
        assertNotNull(roomSearchResult);
        assertEquals(ALL_ROOMS_BOOKED, roomSearchResult.reason());

    }
    //TODO:ADD validator
/*    @Test
    void testFetchAvailableRoomsBookingTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException = assertThrows(InvalidBookingTimingsException.class,
                                                                                     () -> bookingService.fetchAvailableRooms(
                                                                                             LocalTime.parse("24:30"),
                                                                                             LocalTime.parse("09:30")));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }*/

    @Test
    void testFetchAvailableRoomsMaintenanceTimeOverlapping() throws NoSuitableRoomsException, InvalidTimeException {
        setMaintenanceTime();
        RoomSearchResult roomSearchResult = bookingManager.fetchAvailableRooms(new Interval(LocalTime.of(8, 30), LocalTime.of(9, 30)));
        assertNotNull(roomSearchResult);
        assertEquals(OVERLAP_WITH_MAINTENANCE_TIME, roomSearchResult.reason());
    }
    //TODO:validator
/*    @Test
    void testReserveRoomWithIncorrectTimings() {
        InvalidBookingTimingsException invalidBookingTimingsException = assertThrows(InvalidBookingTimingsException.class,
                                                                                     () -> bookingService.reserveRoom(
                                                                                             TestUtils
                                                                                             .fetchBookingRoomRequestWithInvalidTimings()));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }*/

    @Test
    void testReserveRoomWithMaintenanceTimings() {

        setMaintenanceTime();
        Interval interval = new Interval(LocalTime.of(9, 0), LocalTime.of(9, 30));
        MaintenanceTimeOverlapException maintenanceTimingException = assertThrows(MaintenanceTimeOverlapException.class,
                                                                                  () -> bookingManager.reserveRoom(
                                                                                          new BookingRequest(interval, 5)));
        assertNotNull(maintenanceTimingException);
        assertEquals("Requested interval: %s is overlapping with Maintenance time".formatted(interval),
                     maintenanceTimingException.getMessage());
    }

    @Test
    void testReserveRoomWithOneRoomAvailable() throws NoSuitableRoomsException, MaintenanceTimeOverlapException {
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(TestUtils.fetchThreeRoomBookings());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        Room room = bookingManager.reserveRoom(TestUtils.fetchBookingRequestPayload());
        assertNotNull(room);
        assertEquals("BEAUTY", room.name());
    }

    @Test
    void testReserveRoomWithNoRoomAvailable() {
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(TestUtils.allBookings());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        NoSuitableRoomsException noSuitableRoomsException = assertThrows(NoSuitableRoomsException.class, () -> bookingManager.reserveRoom(
                TestUtils.fetchBookingRequestPayload()));
        assertEquals("No Rooms Available for the no of persons 7", noSuitableRoomsException.getMessage());
    }

    @Test
    void testReserveRoomWithTwoRoomsAvailable() throws NoSuitableRoomsException, MaintenanceTimeOverlapException {
        BookingRequest bookingRoomRequest = new BookingRequest(new Interval(LocalTime.of(7, 0), LocalTime.of(9, 0)), 3);
        setMaintenanceTime();
        when(bookingService.findBookings(any())).thenReturn(TestUtils.fetchBookingsTwoAvailable());
        when(roomService.fetchAllRooms()).thenReturn(fetchAllRooms());
        Room room = bookingManager.reserveRoom(bookingRoomRequest);
        assertNotNull(room);
        assertEquals(AMAZE.name(), room.name());
    }
}

