package com.booking.reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "size")
    private String size;
}
