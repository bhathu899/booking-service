package com.booking.reservation.repository;

import com.booking.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface RoomRepository extends JpaRepository<Room,Long> {
}
