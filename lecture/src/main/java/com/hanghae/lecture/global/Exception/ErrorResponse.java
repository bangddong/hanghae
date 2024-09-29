package com.hanghae.lecture.global.Exception;

public record ErrorResponse(
        String code,
        String message
) {
}
