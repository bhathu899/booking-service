package com.conferenceroom.booking.entity;

import com.conferenceroom.booking.enums.ConferenceRoomEnum;
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
@Table(name = "conference_room_booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceRoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    @Column(name = "people_size")
    private int noOfPeople;

    public ConferenceRoomBooking(String startTime, String endTime, String roomName, int noOfPeople){
        this.startTime = Time.valueOf(LocalTime.parse(startTime));
        this.endTime = Time.valueOf(LocalTime.parse(endTime));
        this.noOfPeople = noOfPeople;
        this.roomName = roomName;
    }

    public ConferenceRoomBooking(String startTime, String endTime, ConferenceRoomEnum conferenceRoomEnum){
        this.startTime = Time.valueOf(LocalTime.parse(startTime));
        this.endTime = Time.valueOf(LocalTime.parse(endTime));
        this.noOfPeople = conferenceRoomEnum.getSize();
        this.roomName = conferenceRoomEnum.getName();
    }
}
