package com.room.booking.repository;

import com.room.booking.entity.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {

    List<RoomBooking> findRoomBookingWhereBookingStartTimeBetweenStartTimeAndEndTimeAndBookingEndTimeBetweenStartTimeAndEndTime(Time bookingStartTime , Time bookingEndTime);

}
