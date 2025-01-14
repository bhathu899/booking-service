package com.booking.api.controller.dto;

import java.util.List;

public record AvailableRoomsDto(List<Room> rooms) {

    public record Room(String name, int size) {}
}
