package com.conferenceroom.booking.entity;

import com.conferenceroom.booking.enums.ConferenceRoomEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by KrishnaKo on 19/01/2024
 */
@Entity
@Table(name = "conference_room")
@Getter
@Setter
@NoArgsConstructor
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_size")
    private int size;

    public ConferenceRoom(ConferenceRoomEnum conferenceRoomEnum) {
        this.roomName = conferenceRoomEnum.getName();
        this.size = conferenceRoomEnum.getSize();
    }
}
