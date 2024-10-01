package com.hanghae.architecture.interfaces.dto;


import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.domain.reservation.Reservation;

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
