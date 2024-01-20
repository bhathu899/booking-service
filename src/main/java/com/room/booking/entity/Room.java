package com.room.booking.entity;

import com.room.booking.enums.RoomBookingEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_size")
    private int size;

    public Room(RoomBookingEnum roomBookingEnum) {
        this.roomName = roomBookingEnum.getName();
        this.size = roomBookingEnum.getSize();
    }
}
