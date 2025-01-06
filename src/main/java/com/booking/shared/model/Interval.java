package com.booking.shared.model;

import lombok.Builder;

import java.time.LocalTime;

/**
 * Created by KrishnaKo on 08/12/2024
 */
@Builder
public record Interval(LocalTime startTimeInclusive, LocalTime endTimeExclusive) {}
