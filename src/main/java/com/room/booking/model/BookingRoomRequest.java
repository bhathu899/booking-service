package com.room.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by KrishnaKo on 19/01/2024
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRoomRequest {
    private String startTime;
    private String endTime;
    private int noOfPersons;
}
