package io.hhplus.tdd.global.exception;

public record ErrorResponse(
        String code,
        String message
) {
}
