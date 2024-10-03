package com.hanghae.architecture.infrastructure.repository;

import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationRepositoryJpaAdapter implements ReservationRepository {
    private final JpaReservationRepository jpaReservationRepository;

    @Override
    public List<Reservation> findByUserId(long userId) {
        return jpaReservationRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndLecture(long userId, long scheduleId) {
        return jpaReservationRepository.existsByUserIdAndScheduleId(userId, scheduleId);
    }

    @Override
    public void save(Reservation reservation) {
        jpaReservationRepository.save(reservation);
    }

    @Override
    public long countByScheduleId(Long id) {
        return jpaReservationRepository.countByScheduleId(id);
    }
}
