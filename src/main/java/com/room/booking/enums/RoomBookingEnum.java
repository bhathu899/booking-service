package com.room.booking.enums;

import lombok.Getter;

/**
 * Created by KrishnaKo on 20/01/2024
 */
@Getter
public enum RoomBookingEnum {
    AMAZE("Amaze", 3),
    BEAUTY("Beauty", 7),
    INSPIRE("Inspire", 12),
    STRIVE("Strive", 20);
    private final String name;
    private final int size;

    RoomBookingEnum(final String name, final int size) {
        this.name = name;
        this.size = size;
    }
}
