package com.booking.persistence.service.impl;

import com.booking.persistence.entity.RoomEntity;
import com.booking.persistence.repository.RoomRepository;
import com.booking.persistence.service.RoomService;
import com.booking.persistence.service.model.Room;
import com.booking.persistence.service.model.RoomSearchResult;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    public RoomSearchResult fetchAllRooms() {
        List<RoomEntity> roomList = roomRepository.findAll();
        return mapEntityToDto(roomList);
    }

    private RoomSearchResult mapEntityToDto(List<RoomEntity> rooms) {
        List<Room> roomList = rooms.stream()
                                   .map(room -> new Room(room.getId(),
                                                         room.getRoomName(),
                                                         room.getSize()))
                                   .collect(Collectors.toList());
        return RoomSearchResult.rooms(roomList);
    }
}
