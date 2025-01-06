package com.booking.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalTime;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Entity
@Table(name = "room_booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @Column(name = "room_id") private int roomId;

    @Column(name = "start_time") private Time startTime;

    @Column(name = "end_time") private Time endTime;

    @Column(name = "people_size") private int noOfPeople;

    public BookingEntity(LocalTime startTime, LocalTime endTime, int roomId, int noOfPeople) {
        this.startTime = Time.valueOf(startTime);
        this.endTime = Time.valueOf(endTime);
        this.noOfPeople = noOfPeople;
        this.roomId = roomId;
    }

}
