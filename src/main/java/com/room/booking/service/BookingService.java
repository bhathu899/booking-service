package com.room.booking.service;

import com.room.booking.entity.Room;
import com.room.booking.model.BookingRoomRequest;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface BookingService {

     List<Room> fetchAvailableRooms (String startTime, String endTime);

    Room reserveRoom(BookingRoomRequest bookingRoomRequest);
}
