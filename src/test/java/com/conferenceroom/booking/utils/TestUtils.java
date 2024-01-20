package com.conferenceroom.booking.utils;



import com.conferenceroom.booking.entity.ConferenceRoom;
import com.conferenceroom.booking.entity.ConferenceRoomBooking;
import com.conferenceroom.booking.model.ConferenceRoomBookingRequest;

import java.util.ArrayList;
import java.util.List;

import static com.conferenceroom.booking.enums.ConferenceRoomEnum.AMAZE;
import static com.conferenceroom.booking.enums.ConferenceRoomEnum.BEAUTY;
import static com.conferenceroom.booking.enums.ConferenceRoomEnum.INSPIRE;
import static com.conferenceroom.booking.enums.ConferenceRoomEnum.STRIVE;

/**
 * Created by KrishnaKo on 20/01/2024
 */
public class TestUtils {

    public static ConferenceRoomBookingRequest fetchBookingRoomRequestPayload() {
        return ConferenceRoomBookingRequest.builder().startTime("07:00").endTime("09:00").noOfPersons(7).build();
    }

    public static ConferenceRoomBookingRequest fetchBookingRoomRequestWithInvalidNoOfPersons() {
        return ConferenceRoomBookingRequest.builder().startTime("21:00").endTime("23:30").noOfPersons(25).build();
    }

    public static ConferenceRoomBookingRequest fetchBookingRoomRequestWithMaintenanceTimings() {
        return ConferenceRoomBookingRequest.builder().startTime("09:00").endTime("09:30").noOfPersons(5).build();
    }
    public static ConferenceRoomBookingRequest fetchBookingRoomRequestWithInvalidTimings() {
        return ConferenceRoomBookingRequest.builder().startTime("09:00").endTime("25:30").noOfPersons(5).build();
    }

    public static ConferenceRoom fetchBeautyRoom() {
        return new ConferenceRoom(BEAUTY);
    }

    public static List<ConferenceRoom> fetchAllRooms() {
        List<ConferenceRoom> roomList = new ArrayList<>();
        roomList.add(new ConferenceRoom(AMAZE));
        roomList.add(fetchBeautyRoom());
        roomList.add(new ConferenceRoom(INSPIRE));
        roomList.add(new ConferenceRoom(STRIVE));
        return roomList;
    }

    public static List<ConferenceRoomBooking> fetchRoomBookings() {
        List<ConferenceRoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new ConferenceRoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "12:00", INSPIRE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "13:00", AMAZE));
        return roomBookings;
    }

    public static List<ConferenceRoomBooking> allRoomsBooked() {
        List<ConferenceRoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new ConferenceRoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "12:00", INSPIRE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "13:00", AMAZE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "10:00", BEAUTY));
        return roomBookings;
    }

    public static List<ConferenceRoomBooking> fetchRoomBookingsTwoAvailable() {
        List<ConferenceRoomBooking> roomBookings = new ArrayList<>();
        roomBookings.add(new ConferenceRoomBooking("07:00", "11:00", STRIVE));
        roomBookings.add(new ConferenceRoomBooking("07:00", "12:00", INSPIRE));
        return roomBookings;
    }
}
