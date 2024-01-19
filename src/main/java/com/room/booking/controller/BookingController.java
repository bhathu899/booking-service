package com.booking.reservation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Slf4j
@RestController
@RequestMapping("/v1/booking/rooms/")
public class BookingController {

    @PostMapping("/reserve")
   public ResponseEntity bookRoom(){
    return null;}

    @GetMapping("/available")
    public ResponseEntity fetchAvailableRooms(@RequestParam String startTime,@RequestParam String endTime){

        return null;
    }

}
