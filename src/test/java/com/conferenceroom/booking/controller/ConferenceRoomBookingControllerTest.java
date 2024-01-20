package com.conferenceroom.booking.controller;

import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.service.ConferenceRoomBookingService;
import com.conferenceroom.booking.utils.TestUtils;
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
class ConferenceRoomBookingControllerTest {

    @Mock
    ConferenceRoomBookingService bookingService;

    @InjectMocks
    ConferenceRoomBookingController conferenceRoomBookingController;


    @Test()
    void testBookingRoom() {
        when(bookingService.reserveRoom(any())).thenReturn(TestUtils.fetchBeautyRoom());
        ResponseEntity<ConferenceRoom> roomResponseEntity  = conferenceRoomBookingController.bookRoom(TestUtils.fetchBookingRoomRequestPayload());
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        ConferenceRoom room = roomResponseEntity.getBody();
        assertNotNull(room);
        assertEquals("Beauty",room.getRoomName());
    }

    @Test()
    void testAvailableRooms() {
        when(bookingService.fetchAvailableRooms(anyString(),anyString())).thenReturn(List.of(TestUtils.fetchBeautyRoom()));
        ResponseEntity<List<ConferenceRoom>> roomResponseEntity = conferenceRoomBookingController.fetchAvailableRooms("12:00","23:00");
        assertNotNull(roomResponseEntity);
        assertTrue(roomResponseEntity.hasBody());
        List<ConferenceRoom> roomList = roomResponseEntity.getBody();
        assertTrue(roomList!=null&&roomList.size()>0);
        ConferenceRoom room = roomList.get(0);
        assertEquals("Beauty",room.getRoomName());

    }


}
