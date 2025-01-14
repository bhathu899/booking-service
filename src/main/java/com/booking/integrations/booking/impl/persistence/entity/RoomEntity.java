package com.booking.integrations.booking.impl.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
public class RoomEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name",nullable = false)
    private String roomName;

    @Column(name = "room_size",nullable = false)
    private int size;

}
