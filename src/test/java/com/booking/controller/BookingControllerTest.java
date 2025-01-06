/*
package com.booking.controller;

import com.booking.database.entity.Room;
import com.booking.service.BookingManager;
import com.booking.service.BookingService;
import com.booking.utils.TestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class BookingControllerTest {

    @Mock BookingManager bookingManager;

    @InjectMocks BookingController bookingController;


    @Test()
    void testBookingRoom() {
        when(bookingManager.reserveRoom(any())).thenReturn(TestUtils.fetchBeautyBooking());
        ResponseEntity<Room> roomResponseEntity = bookingController.bookRoom(
                TestUtils.fetchBookingRoomRequestPayload());
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        Room room = roomResponseEntity.getBody();
        assertNotNull(room);
        assertEquals("Beauty", room.getRoomName());
    }

    @Test()
    void testAvailableRooms() {
        when(bookingService.fetchAvailableRooms(anyString(), anyString())).thenReturn(
                List.of(TestUtils.fetchBeautyRoom()));
        ResponseEntity<List<Room>> roomResponseEntity = bookingController.fetchAvailableRooms(
                "12:00", "23:00");
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        List<Room> rooms = roomResponseEntity.getBody();
        assertTrue(rooms != null && rooms.size() > 0);
        Room room = rooms.get(0);
        assertEquals("Beauty", room.getRoomName());

    }


}
*/
