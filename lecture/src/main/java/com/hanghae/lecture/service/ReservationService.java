package com.hanghae.lecture.service;

import com.hanghae.lecture.domain.Lecture;
import com.hanghae.lecture.domain.Reservation;
import com.hanghae.lecture.dto.ReservationResponse;
import com.hanghae.lecture.dto.ReserveRequest;
import com.hanghae.lecture.repository.LectureRepository;
import com.hanghae.lecture.repository.ReservationRepository;
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
