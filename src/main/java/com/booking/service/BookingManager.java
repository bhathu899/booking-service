package com.booking.service;

import com.booking.persistence.service.model.Room;
import com.booking.persistence.service.model.RoomSearchResult;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoRoomsAvailableException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.model.Interval;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface BookingManager {

    RoomSearchResult fetchAvailableRooms(Interval interval) throws NoRoomsAvailableException;

    Room reserveRoom(BookingRequest bookingRequestDto) throws MaintenanceTimeOverlapException, NoRoomsAvailableException;
}
