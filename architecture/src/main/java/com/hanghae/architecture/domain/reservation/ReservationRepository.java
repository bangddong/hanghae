package com.hanghae.architecture.domain.reservation;

import com.hanghae.architecture.domain.lecture.Lecture;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByUserId(long userId);

    long countByLecture(Lecture lecture);

    boolean existsByUserIdAndLecture(long userId, Lecture lecture);

    void save(Reservation of);
}
