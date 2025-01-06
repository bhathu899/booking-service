package com.booking.controller.dto;

import java.util.List;

/**
 * Created by KrishnaKo on 07/12/2024
 */
public record AvailableRoomsDto(List<Room> rooms) {

    public record Room(String name, int size) {}
}
