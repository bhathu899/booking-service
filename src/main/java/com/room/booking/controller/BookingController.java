package com.room.booking.controller;

import com.room.booking.entity.Room;
import com.room.booking.model.BookingRoomRequest;
import com.room.booking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Slf4j
@RestController
@RequestMapping("/v1/booking/rooms/")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/reserve")
   public ResponseEntity<Room> bookRoom(@RequestBody BookingRoomRequest bookingRoomRequest){
        return ResponseEntity.ok(bookingService.reserveRoom(bookingRoomRequest));
      }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> fetchAvailableRooms(@RequestParam String startTime, @RequestParam String endTime){
        return ResponseEntity.ok(bookingService.fetchAvailableRooms(startTime, endTime));
    }

}
