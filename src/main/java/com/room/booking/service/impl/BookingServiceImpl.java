package com.booking.reservation.service.impl;

import com.booking.reservation.config.MaintenanceTimeConfig;
import com.booking.reservation.entity.Room;
import com.booking.reservation.entity.RoomBooking;
import com.booking.reservation.repository.RoomBookingRepository;
import com.booking.reservation.repository.RoomRepository;
import com.booking.reservation.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public class BookingServiceImpl implements BookingService {
    @Autowired
    MaintenanceTimeConfig maintenanceTimeConfig;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomBookingRepository roomBookingRepository;

    @Override
    public List<Room> fetchAvailableRooms(String startTime,String endTime) {
      if(isOverlappingWithMaintenanceTime(null,null)){
          throw new RuntimeException("Overlapping Maintenance Times");
      }
        List<RoomBooking> roomBookingsList = checkForBookedRooms(startTime,endTime);
        return checkForVacantRooms(roomBookingsList);
    }

    private List<Room> checkForVacantRooms(List<RoomBooking> roomBookingsList) {
        List<Room> allRooms = fetchAllRooms();
        return allRooms.stream().filter(room->checkForRoomAvailability(room,roomBookingsList)).collect(Collectors.toList());
    }

    private boolean checkForRoomAvailability(Room room, List<RoomBooking> roomBookingsList) {
        return roomBookingsList.stream().anyMatch(roomBooking ->room.getRoomName().equals(roomBooking.getRoomName()));
    }

    private List<RoomBooking> checkForBookedRooms(String startTime, String endTime) {
    return roomBookingRepository.findRoomBookingWhereBookingStartTimeBetweenStartTimeAndEndTimeAndBookingEndTimeBetweenStartTimeAndEndTime(Time.valueOf(LocalTime.parse(startTime)),
        Time.valueOf(LocalTime.parse(endTime)));
    }

    @Override
    public Room reserveRoom() {
        return null;
    }

    private boolean isOverlappingWithMaintenanceTime(String startTime, String endTime) {
        return maintenanceTimeConfig.getMaintenanceTiming().stream().anyMatch(str -> getMaintenanceStartAndEndPeriods(str.split("-"), startTime, endTime));
    }

    private boolean getMaintenanceStartAndEndPeriods(String[] split,String startTime,String endTime){
        LocalTime maintenanceStartTime = LocalTime.parse(split[0]);
        LocalTime maintenanceEndTime = LocalTime.parse(split[1]);
        LocalTime startTimeLocalTime = LocalTime.parse(startTime);
        LocalTime endTimeLocalTime = LocalTime.parse(endTime);
    return startTimeLocalTime.isBefore(maintenanceEndTime) && maintenanceStartTime.isBefore(endTimeLocalTime);
    }

    @Cacheable("rooms")
    public List<Room> fetchAllRooms(){
        return roomRepository.findAll();
    }
}
