package com.booking.integrations.booking.impl.persistence.repository;

import com.booking.integrations.booking.impl.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository<RoomEntity, Long> {}
