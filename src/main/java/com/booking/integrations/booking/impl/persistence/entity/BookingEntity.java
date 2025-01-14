package com.booking.integrations.booking.impl.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Time;
import java.time.LocalTime;


@Entity
@Table(name = "room_booking")
@Getter
@Setter
@NoArgsConstructor
public class BookingEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //add foreign key constraints to room table
    @Column(name = "room_id",nullable = false)
    private Long roomId;

    @Column(name = "start_time",nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time",nullable = false)
    private LocalTime endTime;

    @Column(name = "people_size",nullable = false)
    @Min(2)
    @Max(20)
    private int noOfPeople;

    public BookingEntity(Long roomId, LocalTime startTime, LocalTime endTime, int noOfPeople) {
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noOfPeople = noOfPeople;
    }
}
