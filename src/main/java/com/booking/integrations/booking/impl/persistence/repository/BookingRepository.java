package com.booking.integrations.booking.impl.persistence.repository;

import com.booking.integrations.booking.impl.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query(value = "SELECT * FROM conference_room_booking WHERE start_time BETWEEN " +
                   ":bookingStartTime and :bookingEndTime OR end_time BETWEEN :bookingStartTime " +
                   "and :bookingEndTime", nativeQuery = true)
    List<BookingEntity> findBookings(@Param("bookingStartTime") LocalTime bookingStartTime,
                                     @Param("bookingEndTime") LocalTime bookingEndTime);

}
