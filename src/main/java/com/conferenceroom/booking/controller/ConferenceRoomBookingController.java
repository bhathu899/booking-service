package com.conferenceroom.booking.controller;

import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.service.ConferenceRoomBookingService;
import com.conferenceroom.booking.model.ConferenceRoomBookingRequest;
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
@RequestMapping("/v1/conferenceroom/booking")
public class ConferenceRoomBookingController {

    @Autowired
    ConferenceRoomBookingService conferenceRoomBookingService;


    @PostMapping("/reserve")
   public ResponseEntity<ConferenceRoom> bookRoom(@RequestBody ConferenceRoomBookingRequest conferenceRoomBookingRequest){
        return ResponseEntity.ok(conferenceRoomBookingService.reserveRoom(conferenceRoomBookingRequest));
      }

    @GetMapping("/available")
    public ResponseEntity<List<ConferenceRoom>> fetchAvailableRooms(@RequestParam String startTime, @RequestParam String endTime){
        return ResponseEntity.ok(conferenceRoomBookingService.fetchAvailableRooms(startTime, endTime));
    }

}
