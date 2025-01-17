package com.booking.service;

import com.booking.integrations.booking.impl.BookingServiceImpl;
import com.booking.integrations.booking.impl.RoomServiceImpl;
import com.booking.integrations.booking.impl.persistence.entity.BookingEntity;
import com.booking.integrations.booking.impl.persistence.repository.BookingRepository;
import com.booking.integrations.booking.impl.persistence.repository.RoomRepository;
import com.booking.integrations.booking.service.model.Booking;
import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.exception.NoSuitableRoomsException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @InjectMocks RoomServiceImpl roomService;

    @Mock RoomRepository roomRepository;

    @Test
    void testFindBookingWithAllRoomsAreBooked() {
        when(roomRepository.findAll()).thenReturn(TestUtils.fetchAllRoomEntities());
        RoomSearchResult roomSearchResult = roomService.fetchAllRooms();
        assertNotNull(roomSearchResult);
        assertEquals(4, roomSearchResult.rooms()
                                        .size());

    }
}
