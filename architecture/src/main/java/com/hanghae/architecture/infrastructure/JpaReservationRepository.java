package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
    long countByLecture(Lecture lecture);
    List<Reservation> findByUserId(long userId);
    boolean existsByUserIdAndLecture(long userId, Lecture lecture);
}
