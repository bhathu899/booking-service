package com.conferenceroom.booking.service;

import com.conferenceroom.booking.config.MaintenanceTimeConfig;
import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.entity.ConferenceRoomBooking;
import com.conferenceroom.booking.exception.InvalidBookingTimingsException;
import com.conferenceroom.booking.exception.MaintenanceTimingException;
import com.conferenceroom.booking.exception.NoRoomsAvailableException;
import com.conferenceroom.booking.model.ConferenceRoomBookingRequest;
import com.conferenceroom.booking.repository.ConferenceRoomBookingRepository;
import com.conferenceroom.booking.repository.ConferenceRoomRepository;
import com.conferenceroom.booking.service.impl.ConferenceRoomBookingServiceImpl;
import com.conferenceroom.booking.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.conferenceroom.booking.enums.ConferenceRoomEnum.AMAZE;
import static com.conferenceroom.booking.enums.ConferenceRoomEnum.BEAUTY;
import static com.conferenceroom.booking.exception.ErrorCodesEnum.INVALID_BOOKING_TIMINGS;
import static com.conferenceroom.booking.exception.ErrorCodesEnum.MAINTENANCE_TIMINGS_ERROR;
import static com.conferenceroom.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING;
import static com.conferenceroom.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING_AND_PERSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by KrishnaKo on 20/01/2024
 */
@ExtendWith(MockitoExtension.class)
public class ConferenceRoomBookingServiceImplTest {

    @InjectMocks
    ConferenceRoomBookingServiceImpl bookingService;

    @Mock
    ConferenceRoomRepository roomRepository;

    @Mock
    ConferenceRoomBookingRepository roomBookingRepository;

    @Mock
    MaintenanceTimeConfig maintenanceTimeConfig;

    @Test
    void testFetchAvailableRooms() {
        List<ConferenceRoomBooking> roomBookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(roomBookings);
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        List<ConferenceRoom> roomList = bookingService.fetchAvailableRooms("08:00", "09:00");
        assertFalse(roomList.isEmpty());
        assertEquals(4, roomList.size());

    }

    @Test
    void testFetchAvailableRoomsWithFewOccupied() {
        List<ConferenceRoomBooking> roomBookings = new ArrayList<>();
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        List<ConferenceRoom> roomList = bookingService.fetchAvailableRooms("08:00", "09:00");
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
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(), maintenanceTimingException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.fetchAvailableRooms("24:30", "09:30"));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testFetchAvailableRoomsBookingEndTimeException() {
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.fetchAvailableRooms("08:30", "09:90"));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testReserveRoomWithMaintenanceTimings() {

        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        MaintenanceTimingException maintenanceTimingException =
                assertThrows(MaintenanceTimingException.class, () -> bookingService.reserveRoom(TestUtils.fetchBookingRoomRequestWithMaintenanceTimings()));
        assertNotNull(maintenanceTimingException);
        assertEquals(MAINTENANCE_TIMINGS_ERROR.getMessage(), maintenanceTimingException.getMessage());
    }

    @Test
    void testReserveRoomWithIncorrectTimings() {
        InvalidBookingTimingsException invalidBookingTimingsException =
                assertThrows(InvalidBookingTimingsException.class, () -> bookingService.reserveRoom(TestUtils.fetchBookingRoomRequestWithInvalidTimings()));
        assertNotNull(invalidBookingTimingsException);
        assertEquals(INVALID_BOOKING_TIMINGS.getMessage(), invalidBookingTimingsException.getMessage());
    }

    @Test
    void testReserveRoomWithNoRoomsAvailableException() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.allRoomsBooked());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        NoRoomsAvailableException noRoomsAvailableException =
                assertThrows(NoRoomsAvailableException.class, () -> bookingService.reserveRoom(TestUtils.fetchBookingRoomRequestPayload()));
        assertNotNull(noRoomsAvailableException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING.getMessage(), noRoomsAvailableException.getMessage());

    }

    @Test
    void testReserveRoomWithNoRoomAvailableForPersons() {
        ConferenceRoomBookingRequest bookingRoomRequest = TestUtils.fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(8);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        NoRoomsAvailableException noRoomsAvailableException =
                assertThrows(NoRoomsAvailableException.class, () -> bookingService.reserveRoom(bookingRoomRequest));
        assertNotNull(noRoomsAvailableException);
        assertEquals(NO_ROOMS_AVAILABLE_TIMING_AND_PERSON.getMessage(), noRoomsAvailableException.getMessage());

    }

    @Test
    void testReserveRoomWithOneRoomAvailable() {
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookings());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        ConferenceRoom room = bookingService.reserveRoom(TestUtils.fetchBookingRoomRequestPayload());
        assertNotNull(room);
        assertEquals(BEAUTY.getName(), room.getRoomName());
    }

    @Test
    void testReserveRoomWithTwoRoomsAvailable() {
        ConferenceRoomBookingRequest bookingRoomRequest = TestUtils.fetchBookingRoomRequestPayload();
        bookingRoomRequest.setNoOfPersons(3);
        when(maintenanceTimeConfig.getTimings()).thenReturn(Collections.singletonList("09:00-09:15"));
        when(roomBookingRepository.findRoomBookingsDuringStartAndEndTime(any(), any())).thenReturn(TestUtils.fetchRoomBookingsTwoAvailable());
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRooms());
        ConferenceRoom room = bookingService.reserveRoom(bookingRoomRequest);
        assertNotNull(room);
        assertEquals(AMAZE.getName(), room.getRoomName());
    }

}
