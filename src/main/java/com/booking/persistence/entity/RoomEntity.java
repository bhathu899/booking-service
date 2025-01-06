package com.booking.persistence.entity;

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
public class RoomEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @Column(name = "room_name") private String roomName;

    @Column(name = "room_size") private int size;

    public RoomEntity(long id, String roomName, int size) {}
}
