package com.booking.reservation.service;

import com.booking.reservation.entity.Room;

import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface BookingService {

     List<Room> fetchAvailableRooms (String startTime,String endTime);

    Room reserveRoom();
}
