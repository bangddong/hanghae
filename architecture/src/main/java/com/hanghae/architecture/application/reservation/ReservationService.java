package com.hanghae.architecture.application.reservation;

import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import com.hanghae.architecture.interfaces.dto.ReservationResponse;
import com.hanghae.architecture.interfaces.dto.ReserveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ScheduleRepository scheduleRepository;

    public List<ReservationResponse> getReservations(long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    public void reserve(long userId, ReserveRequest reserveRequest) {
        final long scheduleId = reserveRequest.scheduleId();

        final boolean alreadyReserved = reservationRepository.existsByUserIdAndLecture(userId, scheduleId);
        if (alreadyReserved) {
            throw new IllegalStateException("이미 예약 된 강의입니다.");
        }

        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));

        schedule.reserve();

        reservationRepository.save(Reservation.of(userId, schedule));
    }
}
