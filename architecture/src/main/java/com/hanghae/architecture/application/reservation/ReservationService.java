package com.hanghae.architecture.application.reservation;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.LectureRepository;
import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import com.hanghae.architecture.interfaces.dto.ReservationResponse;
import com.hanghae.architecture.interfaces.dto.ReserveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final LectureRepository lectureRepository;

    public List<ReservationResponse> getReservations(long userId) {
        return reservationRepository.findByUserId(userId)
                .stream()
                .map(ReservationResponse::fromEntity)
                .toList();
    }

    public void reserve(long userId, ReserveRequest reserveRequest) {
        long lectureId = reserveRequest.lectureId();
        final Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 존재하지 않습니다."));

        long currentCapacity = reservationRepository.countByLecture(lecture);
        if (currentCapacity >= lecture.getCapacity()) {
            throw new IllegalStateException("수강 신청 인원이 초과하였습니다.");
        }

        boolean alreadyReserved = reservationRepository.existsByUserIdAndLecture(userId, lecture);
        if (alreadyReserved) {
            throw new IllegalStateException("이미 예약 된 강의입니다.");
        }

        reservationRepository.save(Reservation.of(userId, lecture));
    }
}
