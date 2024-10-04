package com.hanghae.architecture.interfaces.common;

public record ErrorResponse(
        String code,
        String message
) {
}
