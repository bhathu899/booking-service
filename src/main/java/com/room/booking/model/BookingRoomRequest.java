package com.room.booking.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 2, max = 50)
    @Pattern(regexp = "([01]?\\d|2[0-3]):[0-5]\\d")
    private String startTime;
    @NotEmpty
    @Size(min = 5, max = 5)
    @Pattern(regexp = "([01]?\\d|2[0-3]):[0-5]\\d")
    private String endTime;
    @Size(min = 1, max = 20)
    private int noOfPersons;
}
