package com.hanghae.lecture.repository;

import com.hanghae.lecture.domain.Lecture;
import com.hanghae.lecture.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

     long countByLecture(Lecture lecture);

     boolean existsByUserIdAndLecture(Long userId, Lecture lecture);

     List<Reservation> findByUserId(Long userId);
}
