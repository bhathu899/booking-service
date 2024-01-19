package com.room.booking.controller;

import com.room.booking.entity.Room;
import com.room.booking.model.BookingRoomRequest;
import com.room.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Slf4j
@RestController
@RequestMapping("/v1/booking/rooms/")
@RequiredArgsConstructor
public class BookingController {

    BookingService bookingService;

    @PostMapping("/reserve")
   public ResponseEntity<Room> bookRoom(BookingRoomRequest bookingRoomRequest){
        return ResponseEntity.ok(bookingService.reserveRoom(bookingRoomRequest));
      }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> fetchAvailableRooms(@RequestParam String startTime, @RequestParam String endTime){
        return ResponseEntity.ok(bookingService.fetchAvailableRooms(startTime, endTime));
    }

}
