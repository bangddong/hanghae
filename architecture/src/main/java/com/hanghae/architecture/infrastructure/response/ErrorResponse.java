package com.hanghae.architecture.infrastructure.response;

public record ErrorResponse(
        String code,
        String message
) {
}
