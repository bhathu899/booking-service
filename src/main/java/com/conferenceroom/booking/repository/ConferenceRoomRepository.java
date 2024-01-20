package com.conferenceroom.booking.repository;

import com.conferenceroom.booking.entity.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom,Long> {
}
