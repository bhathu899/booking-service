package com.booking.service.impl;

import com.booking.service.config.MaintenanceTimeConfig;
import com.booking.service.config.MaintenanceTimeConfig.TimeInterval;
import com.booking.integrations.booking.service.model.BookingCommand;
import com.booking.integrations.booking.service.model.FindBookingCommand;
import com.booking.integrations.booking.service.BookingService;
import com.booking.integrations.booking.service.RoomService;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.BookingManager;
import com.booking.service.exception.MaintenanceTimeOverlapException;
import com.booking.integrations.booking.service.exception.AllRoomsAreBookedException;
import com.booking.integrations.booking.service.model.Booking;
import com.booking.service.exception.NoSuitableRoomsException;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.booking.integrations.booking.service.model.RoomSearchResult.EmptyRoomReason.ALL_ROOMS_BOOKED;
import static com.booking.integrations.booking.service.model.RoomSearchResult.EmptyRoomReason.OVERLAP_WITH_MAINTENANCE_TIME;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingManagerImpl implements BookingManager {

    private final MaintenanceTimeConfig maintenanceTimeConfig;
    private final BookingService bookingService;
    private final RoomService roomService;

    @Override
    public RoomSearchResult fetchAvailableRooms(Interval interval) throws NoSuitableRoomsException {
        if (checkIfOverlapsWithMaintenanceTime(interval)) {
            return RoomSearchResult.empty(OVERLAP_WITH_MAINTENANCE_TIME);
        }
        List<Room> rooms = findVacantRooms(interval);
        if (rooms.isEmpty()) {
            return RoomSearchResult.empty(ALL_ROOMS_BOOKED);

        }
        return RoomSearchResult.rooms(rooms);
    }

    private boolean checkIfOverlapsWithMaintenanceTime(Interval requested) {
        return maintenanceTimeConfig.getTimings()
                                    .stream()
                                    .anyMatch(configured -> checkIfIntervalsOverlap(configured, requested));
    }

    private List<Room> findVacantRooms(Interval interval) throws NoSuitableRoomsException {
       try {
           List<Booking> bookings = bookingService.findBookings(new FindBookingCommand(interval.startTimeInclusive(), interval.endTimeExclusive()));
           RoomSearchResult allRooms = roomService.fetchAllRooms();
           return allRooms.rooms()
                          .stream()
                          .filter(room -> checkForRoomAvailability(room, bookings))
                          .toList();
       }catch (AllRoomsAreBookedException exception){
           throw new NoSuitableRoomsException(exception);
       }
    }

    private boolean checkIfIntervalsOverlap(TimeInterval configured, Interval requested) {
        return requested.startTimeInclusive()
                        .isBefore(configured.getEndTime()) && configured.getStartTime()
                                                                        .isBefore(requested.endTimeExclusive());
    }

    private boolean checkForRoomAvailability(Room room, List<Booking> existingBookings) {
        return existingBookings.stream()
                               .noneMatch(conferenceRoomBooking -> room.roomId() == conferenceRoomBooking.roomId());
    }

    @Override
    public Room reserveRoom(BookingRequest request) throws MaintenanceTimeOverlapException, NoSuitableRoomsException {

        Interval interval = request.interval();
        if (checkIfOverlapsWithMaintenanceTime(interval)) {
            throw new MaintenanceTimeOverlapException("Requested interval: %s is overlapping with Maintenance time".formatted(interval));
        }
        List<Room> vacantRooms = findVacantRooms(interval);
        int noOfPersons = request.noOfPersons();
        Room room = findSuitableRoom(vacantRooms, noOfPersons);
        BookingCommand bookingCommand = BookingCommand.builder()
                                                      .roomId(room.roomId())
                                                      .noOfPersons(noOfPersons)
                                                      .interval(interval)
                                                      .build();
        bookingService.saveBooking(bookingCommand);

        return room;
    }

    private Room findSuitableRoom(List<Room> vacantRooms, int noOfPersons) throws NoSuitableRoomsException {
        return vacantRooms.stream()
                          .filter(room -> room.size() >= noOfPersons)
                          .findFirst()
                          .orElseThrow(() -> new NoSuitableRoomsException(
                                  "No Rooms Available for the no of persons %s".formatted(noOfPersons)));

    }

}
