package com.conferenceroom.booking.service.impl;

import com.conferenceroom.booking.config.MaintenanceTimeConfig;
import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.entity.ConferenceRoomBooking;
import com.conferenceroom.booking.exception.ErrorCodesEnum;
import com.conferenceroom.booking.repository.ConferenceRoomBookingRepository;
import com.conferenceroom.booking.repository.ConferenceRoomRepository;
import com.conferenceroom.booking.service.ConferenceRoomBookingService;
import com.conferenceroom.booking.validator.ConferenceRoomBookingTimeValidator;
import com.conferenceroom.booking.exception.MaintenanceTimingException;
import com.conferenceroom.booking.exception.NoRoomsAvailableException;
import com.conferenceroom.booking.model.ConferenceRoomBookingRequest;
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

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Slf4j
@Service
public class ConferenceRoomBookingServiceImpl implements ConferenceRoomBookingService {
    @Autowired
    MaintenanceTimeConfig maintenanceTimeConfig;

    @Autowired
    ConferenceRoomRepository conferenceRoomRepository;

    @Autowired
    ConferenceRoomBookingRepository conferenceRoomBookingRepository;
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true);

    @Override
    public List<ConferenceRoom> fetchAvailableRooms(String startTime, String endTime) {
        validateTimings(startTime, endTime);
      return fetchVacantRooms(startTime,endTime);
    }

    public void validateTimings(String startTime, String endTime){
        ConferenceRoomBookingTimeValidator.validateBookingTimings(startTime, endTime);
        checkForMaintenanceTimings(startTime,endTime);
    }
    private List<ConferenceRoom> fetchVacantRooms(String startTime, String endTime){
        lock.readLock().lock();
        List<ConferenceRoomBooking> conferenceRoomBookingsList = checkForBookedRooms(startTime,endTime);
        List<ConferenceRoom> vacantConferenceRooms;
        try {
            vacantConferenceRooms = checkForVacantRooms(conferenceRoomBookingsList);
        } finally {
            lock.readLock ().unlock ();
        }
        return vacantConferenceRooms;
    }
    private List<ConferenceRoom> checkForVacantRooms(List<ConferenceRoomBooking> conferenceRoomBookingsList) {
        List<ConferenceRoom> allConferenceRooms = fetchAllRooms();
        List<ConferenceRoom> availableConferenceRooms =  allConferenceRooms.stream().filter(conferenceRoom ->checkForRoomAvailability(conferenceRoom, conferenceRoomBookingsList)).toList();
        if(availableConferenceRooms.isEmpty()){
            throw new NoRoomsAvailableException(ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING.getMessage());
        }
        return availableConferenceRooms;
    }

    private boolean checkForRoomAvailability(ConferenceRoom conferenceRoom, List<ConferenceRoomBooking> conferenceRoomBookingsList) {
        return !conferenceRoomBookingsList.stream().anyMatch(conferenceRoomBooking -> conferenceRoom.getRoomName().equals(conferenceRoomBooking.getRoomName()));
    }

    private List<ConferenceRoomBooking> checkForBookedRooms(String startTime, String endTime) {
    return conferenceRoomBookingRepository.findRoomBookingsDuringStartAndEndTime(Time.valueOf(LocalTime.parse(startTime)),
        Time.valueOf(LocalTime.parse(endTime)));
    }

    @Override
    public ConferenceRoom reserveRoom(ConferenceRoomBookingRequest conferenceRoomBookingRequest) {
        String startTime= conferenceRoomBookingRequest.getStartTime();
        String endTime= conferenceRoomBookingRequest.getEndTime();
        validateTimings(startTime, endTime);
        List<ConferenceRoom> vacantConferenceRooms =  fetchVacantRooms(startTime, endTime);
        int noOfPersons = conferenceRoomBookingRequest.getNoOfPersons();
        ConferenceRoom conferenceRoom =  findSuitableRoom(vacantConferenceRooms,noOfPersons);
        lock.writeLock().lock();
       try {
           conferenceRoomBookingRepository.save(new ConferenceRoomBooking(startTime, endTime, conferenceRoom.getRoomName(), noOfPersons));
       }
       finally{
           lock.writeLock().lock();
       }
        return conferenceRoom;
    }

    private void checkForMaintenanceTimings(String startTime,String endTime) {
        if(isOverlappingWithMaintenanceTime(startTime,endTime)){
            throw new MaintenanceTimingException(ErrorCodesEnum.MAINTENANCE_TIMINGS_ERROR.getMessage());
        }
    }

    private ConferenceRoom findSuitableRoom(List<ConferenceRoom> vacantConferenceRooms, int noOfPersons) {

       List<ConferenceRoom> modifiedList = new ArrayList<>(vacantConferenceRooms);
         modifiedList.sort(Comparator.comparing(ConferenceRoom::getSize));
       Optional<ConferenceRoom> roomOptional = modifiedList.stream().filter(conferenceRoom -> conferenceRoom.getSize()>=noOfPersons).findFirst();
       if(roomOptional.isEmpty()){
           throw new NoRoomsAvailableException(ErrorCodesEnum.NO_ROOMS_AVAILABLE_TIMING_AND_PERSON.getMessage());
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
    public List<ConferenceRoom> fetchAllRooms(){
        return conferenceRoomRepository.findAll();
    }
}
