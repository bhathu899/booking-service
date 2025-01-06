package com.booking.integrations.booking.impl.persistence.repository;

import com.booking.integrations.booking.impl.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by KrishnaKo on 19/01/2024
 */
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {}
