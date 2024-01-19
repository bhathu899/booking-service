package com.room.booking.repository;

import com.room.booking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface RoomRepository extends JpaRepository<Room,Long> {
}
