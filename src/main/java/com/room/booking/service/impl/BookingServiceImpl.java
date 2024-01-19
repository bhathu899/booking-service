package com.room.booking.service.impl;

import com.room.booking.config.MaintenanceTimeConfig;
import com.room.booking.entity.Room;
import com.room.booking.entity.RoomBooking;
import com.room.booking.model.BookingRoomRequest;
import com.room.booking.repository.RoomBookingRepository;
import com.room.booking.repository.RoomRepository;
import com.room.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public List<Room> fetchAvailableRooms(String startTime, String endTime) {
      if(isOverlappingWithMaintenanceTime(startTime,endTime)){
          throw new RuntimeException("Overlapping Maintenance Times");
      }
      return fetchVacantRooms(startTime,endTime);
    }

    private List<Room> fetchVacantRooms(String startTime, String endTime){
        List<RoomBooking> roomBookingsList = checkForBookedRooms(startTime,endTime);
        return checkForVacantRooms(roomBookingsList);
    }
    private List<Room> checkForVacantRooms(List<RoomBooking> roomBookingsList) {
        List<Room> allRooms = fetchAllRooms();
        return allRooms.stream().filter(room->checkForRoomAvailability(room,roomBookingsList)).toList();
    }

    private boolean checkForRoomAvailability(Room room, List<RoomBooking> roomBookingsList) {
        return roomBookingsList.stream().anyMatch(roomBooking ->room.getRoomName().equals(roomBooking.getRoomName()));
    }

    private List<RoomBooking> checkForBookedRooms(String startTime, String endTime) {
    return roomBookingRepository.findRoomBookingWhereBookingStartTimeBetweenStartTimeAndEndTimeAndBookingEndTimeBetweenStartTimeAndEndTime(Time.valueOf(LocalTime.parse(startTime)),
        Time.valueOf(LocalTime.parse(endTime)));
    }

    @Override
    public Room reserveRoom(BookingRoomRequest bookingRoomRequest) {
        String startTime= bookingRoomRequest.getStartTime();
        String endTime= bookingRoomRequest.getEndTime();
        if(isOverlappingWithMaintenanceTime(startTime,endTime)){
            throw new RuntimeException("Overlapping Maintenance Times");
        }
        List<Room> vacantRooms =  fetchVacantRooms(startTime, endTime);
        return findSuitableRoom(vacantRooms,bookingRoomRequest.getNoOfPersons());
    }

    private Room findSuitableRoom(List<Room> vacantRooms, String noOfPersons) {

      vacantRooms.sort(Comparator.comparing(Room::getSize));
       Optional<Room> roomOptional = vacantRooms.stream().filter(room->room.getSize()>=Integer.parseInt(noOfPersons)).findFirst();
        return roomOptional.orElseThrow(RuntimeException::new);
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
