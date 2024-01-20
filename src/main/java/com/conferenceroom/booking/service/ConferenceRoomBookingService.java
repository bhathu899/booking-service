package com.conferenceroom.booking.service;

import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.model.ConferenceRoomBookingRequest;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface ConferenceRoomBookingService {

     List<ConferenceRoom> fetchAvailableRooms (String startTime, String endTime);

    ConferenceRoom reserveRoom(ConferenceRoomBookingRequest conferenceRoomBookingRequest);
}
