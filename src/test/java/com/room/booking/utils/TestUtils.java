package com.room.booking.utils;

import com.room.booking.entity.Room;
import com.room.booking.entity.RoomBooking;
import com.room.booking.model.BookingRoomRequest;

import java.util.ArrayList;
import java.util.List;

import static com.room.booking.enums.RoomBookingEnum.AMAZE;
import static com.room.booking.enums.RoomBookingEnum.BEAUTY;
import static com.room.booking.enums.RoomBookingEnum.INSPIRE;
import static com.room.booking.enums.RoomBookingEnum.STRIVE;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class TestUtils {

    public static BookingRoomRequest fetchBookingRoomRequestPayload() {
        return BookingRoomRequest.builder().startTime("07:00").endTime("09:00").noOfPersons(7).build();
    }

    public static BookingRoomRequest fetchBookingRoomRequestWithInvalidNoOfPersons() {
        return BookingRoomRequest.builder().startTime("21:00").endTime("23:30").noOfPersons(25).build();
    }

    public static BookingRoomRequest fetchBookingRoomRequestWithMaintenanceTimings() {
        return BookingRoomRequest.builder().startTime("09:00").endTime("09:30").noOfPersons(5).build();
    }
    public static BookingRoomRequest fetchBookingRoomRequestWithInvalidTimings() {
        return BookingRoomRequest.builder().startTime("09:00").endTime("25:30").noOfPersons(5).build();
    }

    public static Room fetchBeautyRoom() {
        return new Room(BEAUTY);
    }

    public static List<Room> fetchAllRooms() {
        List<Room> roomList = new ArrayList<>();
        roomList.add(new Room(AMAZE));
        roomList.add(fetchBeautyRoom());
        roomList.add(new Room(INSPIRE));
        roomList.add(new Room(STRIVE));
        return roomList;
    }

    public static List<RoomBooking> fetchRoomBookings() {
        List<RoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new RoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new RoomBooking("07:00", "12:00", INSPIRE));
        roomBookings.add(new RoomBooking("07:00", "13:00", AMAZE));
        return roomBookings;
    }

    public static List<RoomBooking> allRoomsBooked() {
        List<RoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new RoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new RoomBooking("07:00", "12:00", INSPIRE));
        roomBookings.add(new RoomBooking("07:00", "13:00", AMAZE));
        roomBookings.add(new RoomBooking("07:00", "10:00", BEAUTY));
        return roomBookings;
    }

    public static List<RoomBooking> fetchRoomBookingsTwoAvailable() {
        List<RoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new RoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new RoomBooking("07:00", "12:00", INSPIRE));
        return roomBookings;
    }
}
