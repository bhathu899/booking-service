package com.booking.errors;

import lombok.Builder;

/**
 * Created by KrishnaKo on 20/01/2024
 */
@Builder
public record ErrorDto(
        String errorCode, String message
) {}
