package com.booking.api.errors;

import lombok.Builder;


public record ErrorDto(
        String errorCode, String message
) {}
