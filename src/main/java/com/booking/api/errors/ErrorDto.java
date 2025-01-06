package com.booking.api.errors;

import lombok.Builder;

/**
 * Created by KrishnaKo on 20/01/2024
 */

public record ErrorDto(
        String errorCode, String message
) {}
