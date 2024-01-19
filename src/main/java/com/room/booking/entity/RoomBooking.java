package com.booking.reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Entity
@Table(name = "room_booking")
@Data
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "start_time")
    private Time endTime;

    @Column(name = "people_size")
    private String noOfPeople;

}
