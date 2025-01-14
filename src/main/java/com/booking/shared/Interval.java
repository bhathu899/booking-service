package com.booking.shared;


import java.time.LocalTime;


public record Interval(LocalTime startTimeInclusive, LocalTime endTimeExclusive) {}
