package com.booking.persistence;

import java.time.LocalTime;

/**
 * Created by KrishnaKo on 08/12/2024
 */

public record FindBookingCommand(LocalTime startTime, LocalTime endTime) {}
