package com.room.booking.controller;

import com.room.booking.entity.Room;
import com.room.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.room.booking.utils.TestUtils.fetchBeautyRoom;
import static com.room.booking.utils.TestUtils.fetchBookingRoomRequestPayload;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class BookingControllerTest {

    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;


    @Test()
    void testBookingRoom() {
        when(bookingService.reserveRoom(any())).thenReturn(fetchBeautyRoom());
        ResponseEntity<Room> roomResponseEntity  = bookingController.bookRoom(fetchBookingRoomRequestPayload());
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        Room room = roomResponseEntity.getBody();
        assertNotNull(room);
        assertEquals("Beauty",room.getRoomName());
    }

    @Test()
    void testAvailableRooms() {
        when(bookingService.fetchAvailableRooms(anyString(),anyString())).thenReturn(List.of(fetchBeautyRoom()));
        ResponseEntity<List<Room>> roomResponseEntity = bookingController.fetchAvailableRooms("12:00","23:00");
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        List<Room> roomList = roomResponseEntity.getBody();
        assertTrue(roomList!=null&&roomList.size()>0);
        Room room = roomList.get(0);
        assertEquals("Beauty",room.getRoomName());

    }


}
