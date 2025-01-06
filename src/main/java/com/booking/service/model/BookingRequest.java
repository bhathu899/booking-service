package com.booking.service.model;

import com.booking.shared.Interval;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public record BookingRequest(Interval interval, int noOfPersons) {}
