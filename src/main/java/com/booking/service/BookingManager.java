package com.booking.service;

import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.exception.InvalidTimeException;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.service.exception.NoSuitableRoomsException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;


public interface BookingManager {

    RoomSearchResult fetchAvailableRooms(Interval interval) throws NoSuitableRoomsException, InvalidTimeException;

    Room reserveRoom(BookingRequest bookingRequestDto) throws MaintenanceTimeOverlapException,
                                                              NoSuitableRoomsException;
}
