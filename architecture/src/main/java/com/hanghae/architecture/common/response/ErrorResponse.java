package com.hanghae.architecture.common.response;

public record ErrorResponse(
        String code,
        String message
) {
}
