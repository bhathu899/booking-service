package com.booking.utils;


import com.booking.api.controller.dto.BookingRequestDto;
import com.booking.integrations.booking.impl.persistence.entity.BookingEntity;
import com.booking.integrations.booking.impl.persistence.entity.RoomEntity;
import com.booking.integrations.booking.service.model.Booking;
import com.booking.integrations.booking.service.model.FindBookingCommand;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import com.booking.service.model.BookingRequest;
import com.booking.shared.Interval;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TestUtils {
     public static final Room AMAZE = new Room(1L, "AMAZE", 3);
    public static final Room BEAUTY = new Room(2L, "BEAUTY", 7);
    private static final LocalTime LOCAL_TIME = LocalTime.of(7, 0);
     static final Room INSPIRE = new Room(3L, "INSPIRE", 12);
     static final Room STRIVE = new Room(4L, "STRIVE", 20);

    public static BookingRequestDto fetchBookingRoomRequestPayload() {
        return BookingRequestDto.builder()
                                .startTime(LOCAL_TIME)
                                .endTime(LocalTime.of(9, 0))
                                .noOfPersons(7)
                                .build();
    }

    public static BookingRequest fetchBookingRequestPayload() {
        return new BookingRequest(new Interval(LOCAL_TIME,
                                LocalTime.of(9, 0)),
                              7);
    }

    public static FindBookingCommand fetchFindBookingCommand() {
        return new FindBookingCommand(LOCAL_TIME,
                                                   LocalTime.of(9, 0));
    }
    public static BookingRequestDto fetchBookingRoomRequestWithInvalidNoOfPersons() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.of(21, 0))
                                .endTime(LocalTime.of(23, 20))
                                .noOfPersons(25)
                                .build();
    }

    public static BookingRequestDto fetchBookingRoomRequestWithMaintenanceTimings() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.of(9, 0))
                                .endTime(LocalTime.of(9, 30))
                                .noOfPersons(5)
                                .build();
    }



    public static BookingRequestDto fetchBookingRoomRequestWithInvalidTimings() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.of(9, 0))
                                .endTime(LocalTime.of(25, 30))
                                .noOfPersons(5)
                                .build();
    }


    public static List<Room> fetchAllRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(AMAZE);
        roomList.add(BEAUTY);
        roomList.add(INSPIRE);
        roomList.add(STRIVE);
        return roomList;
    }

    public static List<BookingEntity> fetchRoomBookings() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(new BookingEntity(4L, LOCAL_TIME, LocalTime.of(11, 0), 20));
        bookingEntities.add(new BookingEntity(3L, LOCAL_TIME, LocalTime.of(12, 0), 12));
        bookingEntities.add(new BookingEntity(1L, LOCAL_TIME, LocalTime.of(13, 0), 3));
        return bookingEntities;
    }


    public static List<Booking> fetchThreeRoomBookings() {
        List<Booking> bookingEntities = new ArrayList<>();
        bookingEntities.add(new Booking(4L));
        bookingEntities.add(new Booking(3L));
        bookingEntities.add(new Booking(1L));
        return bookingEntities;
    }
    public static List<Booking> allBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(4L));
        bookings.add(new Booking(3L));
        bookings.add(new Booking(1L));
        bookings.add(new Booking(2L));
        return bookings;
    }
    public static List<BookingEntity> allRoomsBooked() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(new BookingEntity(1L, LOCAL_TIME, LocalTime.of(13, 0), 3));
        bookingEntities.add(new BookingEntity(2L, LOCAL_TIME, LocalTime.of(10, 0), 7));
        bookingEntities.add(new BookingEntity(3L, LOCAL_TIME, LocalTime.of(13, 0), 3));
        bookingEntities.add(new BookingEntity(4L, LOCAL_TIME, LocalTime.of(10, 0), 7));

        return bookingEntities;
    }

    public static List<BookingEntity> fetchBookingEntitiesTwoAvailable() {
        List<BookingEntity> bookingEntities = new ArrayList<>();
        bookingEntities.add(new BookingEntity(4L, LOCAL_TIME, LocalTime.of(11, 0), 20));
        bookingEntities.add(new BookingEntity(3L, LOCAL_TIME, LocalTime.of(12, 0), 12));
        return bookingEntities;
    }

    public static List<Booking> fetchBookingsTwoAvailable() {
        List<Booking> bookingEntities = new ArrayList<>();
        bookingEntities.add(new Booking(4L));
        bookingEntities.add(new Booking(3L));
        return bookingEntities;
    }

    public static Room fetchBeautyBooking() {
        return BEAUTY;
    }

    public static List<RoomEntity> fetchAllRoomEntities() {
        List<RoomEntity> roomEntities = new ArrayList<>();
        roomEntities.add(fetchBeautyRoomEntity());
        roomEntities.add(fetchAmazeRoomEntity());
        roomEntities.add(fetchInspireRoomEntity());
        roomEntities.add(fetchStriveRoomEntity());
        return roomEntities;
    }

    static RoomEntity fetchBeautyRoomEntity(){
        RoomEntity room = new RoomEntity();
        room.setId( 2L);
        room.setRoomName("BEAUTY");
        room.setSize(7);
        return room;
    }

    static RoomEntity fetchInspireRoomEntity(){
        RoomEntity room = new RoomEntity();
        room.setId( 3L);
        room.setRoomName("INSPIRE");
        room.setSize(12);
        return room;
    }
    static RoomEntity fetchAmazeRoomEntity(){
        RoomEntity room = new RoomEntity();
        room.setId( 1L);
        room.setRoomName("AMAZE");
        room.setSize(3);
        return room;
    }
    static RoomEntity fetchStriveRoomEntity(){
        RoomEntity room = new RoomEntity();
        room.setId( 4L);
        room.setRoomName("STRIVE");
        room.setSize(20);
        return room;
    }

}
