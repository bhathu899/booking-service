package com.booking.service;

import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoSuitableRoomsException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface BookingManager {

    RoomSearchResult fetchAvailableRooms(Interval interval) throws NoSuitableRoomsException;

    Room reserveRoom(BookingRequest bookingRequestDto) throws MaintenanceTimeOverlapException,
                                                              NoSuitableRoomsException;
}
