package com.booking.service.model;

import com.booking.shared.model.Interval;
import lombok.Builder;

/**
 * Created by KrishnaKo on 08/12/2024
 */
public record BookingRequest(Interval interval, int noOfPersons) {}
