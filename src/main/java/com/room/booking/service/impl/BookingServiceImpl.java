package com.room.booking.service.impl;

import com.room.booking.config.MaintenanceTimeConfig;
import com.room.booking.entity.Room;
import com.room.booking.entity.RoomBooking;
import com.room.booking.exception.MaintenanceTimingException;
import com.room.booking.exception.NoRoomsAvailableException;
import com.room.booking.model.BookingRoomRequest;
import com.room.booking.repository.RoomBookingRepository;
import com.room.booking.repository.RoomRepository;
import com.room.booking.service.BookingService;
import com.room.booking.validator.BookingTimeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.room.booking.exception.ErrorCodesEnum.MAINTENANCE_TIMINGS_ERROR;
import static com.room.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING;
import static com.room.booking.exception.ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING_AND_PERSON;
import static com.room.booking.validator.BookingTimeValidator.validateBookingTimings;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Slf4j
@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    MaintenanceTimeConfig maintenanceTimeConfig;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomBookingRepository roomBookingRepository;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public List<Room> fetchAvailableRooms(String startTime, String endTime) {
        validateTimings(startTime, endTime);
      return fetchVacantRooms(startTime,endTime);
    }

    public void validateTimings(String startTime, String endTime){
        validateBookingTimings(startTime, endTime);
        checkForMaintenanceTimings(startTime,endTime);
    }
    private List<Room> fetchVacantRooms(String startTime, String endTime){
        lock.readLock().lock();
        List<RoomBooking> roomBookingsList = checkForBookedRooms(startTime,endTime);
        List<Room> vacantRooms;
        try {
            vacantRooms = checkForVacantRooms(roomBookingsList);
        } finally {
            lock.readLock ().unlock ();
        }
        return vacantRooms;
    }
    private List<Room> checkForVacantRooms(List<RoomBooking> roomBookingsList) {
        List<Room> allRooms = fetchAllRooms();
        List<Room> availableRooms =  allRooms.stream().filter(room->checkForRoomAvailability(room,roomBookingsList)).toList();
        if(availableRooms.isEmpty()){
            throw new NoRoomsAvailableException(NO_ROOMS_AVAILABLE_TIMING.getMessage());
        }
        return availableRooms;
    }

    private boolean checkForRoomAvailability(Room room, List<RoomBooking> roomBookingsList) {
        return !roomBookingsList.stream().anyMatch(roomBooking ->room.getRoomName().equals(roomBooking.getRoomName()));
    }

    private List<RoomBooking> checkForBookedRooms(String startTime, String endTime) {
    return roomBookingRepository.findRoomBookingsDuringStartAndEndTime(Time.valueOf(LocalTime.parse(startTime)),
        Time.valueOf(LocalTime.parse(endTime)));
    }

    @Override
    public Room reserveRoom(BookingRoomRequest bookingRoomRequest) {
        String startTime= bookingRoomRequest.getStartTime();
        String endTime= bookingRoomRequest.getEndTime();
        validateTimings(startTime, endTime);
        List<Room> vacantRooms =  fetchVacantRooms(startTime, endTime);
        int noOfPersons = bookingRoomRequest.getNoOfPersons();
        Room room =  findSuitableRoom(vacantRooms,noOfPersons);
        lock.writeLock().lock();
       try {
           roomBookingRepository.save(new RoomBooking(startTime, endTime, room.getRoomName(), noOfPersons));
       }
       finally{
           lock.writeLock().lock();
       }
        return room;
    }

    private void checkForMaintenanceTimings(String startTime,String endTime) {
        if(isOverlappingWithMaintenanceTime(startTime,endTime)){
            throw new MaintenanceTimingException(MAINTENANCE_TIMINGS_ERROR.getMessage());
        }
    }

    private Room findSuitableRoom(List<Room> vacantRooms, int noOfPersons) {

       List<Room> modifiedList = new ArrayList<>(vacantRooms);
         modifiedList.sort(Comparator.comparing(Room::getSize));
       Optional<Room> roomOptional = modifiedList.stream().filter(room->room.getSize()>=noOfPersons).findFirst();
       if(roomOptional.isEmpty()){
           throw new NoRoomsAvailableException(NO_ROOMS_AVAILABLE_TIMING_AND_PERSON.getMessage());
       }
       return roomOptional.get();
    }

    private boolean isOverlappingWithMaintenanceTime(String startTime, String endTime) {
        return maintenanceTimeConfig.getTimings().stream().anyMatch(str -> getMaintenanceStartAndEndPeriods(str.split("-"), startTime, endTime));
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
