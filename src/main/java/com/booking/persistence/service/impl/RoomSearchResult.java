package com.booking.persistence.service.impl;

import lombok.Builder;

import java.util.Collections;
import java.util.List;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record RoomSearchResult(List<Room> rooms, EmptyRoomReason reason) {

    public static RoomSearchResult empty(EmptyRoomReason reason) {
        return new RoomSearchResult(Collections.emptyList(), reason);
    }

    public static RoomSearchResult rooms(List<Room> rooms) {
        return new RoomSearchResult(rooms, null);
    }

    public enum EmptyRoomReason {
        OVERLAP_WITH_MAINTENANCE_TIME, ALL_ROOMS_BOOKED;
    }

}
