package com.booking.service.model;

import com.booking.shared.Interval;


public record BookingRequest(Interval interval, int noOfPersons) {}
