package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final JpaReservationRepository jpaReservationRepository;

    @Override
    public List<Reservation> findByUserId(long userId) {
        return jpaReservationRepository.findByUserId(userId);
    }

    @Override
    public long countByLecture(Lecture lecture) {
        return jpaReservationRepository.countByLecture(lecture);
    }

    @Override
    public boolean existsByUserIdAndLecture(long userId, Lecture lecture) {
        return jpaReservationRepository.existsByUserIdAndLecture(userId, lecture);
    }

    @Override
    public void save(Reservation of) {
        jpaReservationRepository.save(of);
    }
}
