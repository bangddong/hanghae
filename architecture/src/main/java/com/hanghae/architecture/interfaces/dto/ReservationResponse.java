package com.hanghae.architecture.interfaces.dto;


import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.domain.reservation.Reservation;

public record ReservationResponse(
        Long id,
        String tutor,
        Subject subject,
        String date
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule().getLecture().getTutor(),
                reservation.getSchedule().getLecture().getSubject(),
                reservation.getSchedule().getDate().toString()
        );
    }
}
