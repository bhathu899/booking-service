package com.booking.shared;


import java.time.LocalTime;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public record Interval(LocalTime startTimeInclusive, LocalTime endTimeExclusive) {}
