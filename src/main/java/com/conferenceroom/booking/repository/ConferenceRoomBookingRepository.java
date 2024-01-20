package com.conferenceroom.booking.repository;

import com.conferenceroom.booking.entity.ConferenceRoomBooking;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface ConferenceRoomBookingRepository extends JpaRepository<ConferenceRoomBooking, Long> {

    @Query(value = "SELECT * FROM room_booking WHERE start_time BETWEEN :bookingStartTime and :bookingEndTime OR end_time BETWEEN :bookingStartTime and :bookingEndTime", nativeQuery = true)
    List<ConferenceRoomBooking> findRoomBookingsDuringStartAndEndTime(@Param("bookingStartTime") Time bookingStartTime , @Param("bookingEndTime")  Time bookingEndTime);

}
