package com.booking.integrations.booking.service.model;

import java.time.LocalTime;



public record FindBookingCommand(LocalTime startTime, LocalTime endTime) {}
