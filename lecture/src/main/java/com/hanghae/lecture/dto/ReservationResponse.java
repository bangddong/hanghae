package com.hanghae.lecture.dto;

import com.hanghae.lecture.contant.Subject;
import com.hanghae.lecture.domain.Reservation;

public record ReservationResponse(
        Long id,
        String tutor,
        Subject subject
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getLecture().getTutor(),
                reservation.getLecture().getSubject()
        );
    }
}
