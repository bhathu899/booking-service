package com.booking.integrations.booking.impl;

import com.booking.integrations.booking.impl.persistence.entity.RoomEntity;
import com.booking.integrations.booking.impl.persistence.repository.RoomRepository;
import com.booking.integrations.booking.service.RoomService;
import com.booking.integrations.booking.service.model.Room;
import com.booking.integrations.booking.service.model.RoomSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
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
                                   .toList();
        return RoomSearchResult.rooms(roomList);
    }
}
