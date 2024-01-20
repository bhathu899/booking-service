package com.room.booking.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @NotEmpty
    private String startTime;
    @NotEmpty
    private String endTime;
    @Size(min = 2, max = 20)
    private int noOfPersons;
}
