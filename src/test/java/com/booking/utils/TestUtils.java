package com.booking.utils;


import com.booking.controller.dto.BookingRequestDto;
import com.booking.persistence.entity.BookingEntity;
import com.booking.persistence.service.impl.Room;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class TestUtils {

    public static BookingRequestDto fetchBookingRoomRequestPayload() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.parse("07:00"))
                                .endTime(LocalTime.parse("09:00"))
                                .noOfPersons(7)
                                .build();
    }

    public static BookingRequestDto fetchBookingRoomRequestWithInvalidNoOfPersons() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.parse("21:00"))
                                .endTime(LocalTime.parse("23:30"))
                                .noOfPersons(25)
                                .build();
    }

    public static BookingRequestDto fetchBookingRoomRequestWithMaintenanceTimings() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.parse("09:00"))
                                .endTime(LocalTime.parse("09:30"))
                                .noOfPersons(5)
                                .build();
    }

    public static BookingRequestDto fetchBookingRoomRequestWithInvalidTimings() {
        return BookingRequestDto.builder()
                                .startTime(LocalTime.parse("09:00"))
                                .endTime(LocalTime.parse("25:30"))
                                .noOfPersons(5)
                                .build();
    }


    public static List<Room> fetchAllRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(1, "AMAZE", 3));
        roomList.add(new Room(2, "BEAUTY", 7));
        roomList.add(new Room(3, "INSPIRE", 12));
        roomList.add(new Room(4, "STRIVE", 20));
        return roomList;
    }

    public static List<BookingEntity> fetchRoomBookings() {
        List<BookingEntity> bookingÊntities = new ArrayList<>();
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("11:00"), 4, 20));
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("12:00"), 3, 12));
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("13:00"), 1, 3));
        return bookingÊntities;
    }

    public static List<BookingEntity> allRoomsBooked() {
        List<BookingEntity> bookingÊntities = new ArrayList<>();
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("13:00"), 1, 3));
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("10:00"), 2, 7));
        return bookingÊntities;
    }

    public static List<BookingEntity> fetchRoomBookingsTwoAvailable() {
        List<BookingEntity> bookingÊntities = new ArrayList<>();
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("11:00"), 4, 20));
        bookingÊntities.add(
                new BookingEntity(LocalTime.parse("07:00"), LocalTime.parse("12:00"), 3, 12));
        return bookingÊntities;
    }
}
