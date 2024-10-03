package com.hanghae.architecture.infrastructure;

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
    public boolean existsByUserIdAndLecture(long userId, long scheduleId) {
        return jpaReservationRepository.existsByUserIdAndScheduleId(userId, scheduleId);
    }

    @Override
    public void save(Reservation reservation) {
        jpaReservationRepository.save(reservation);
    }
}
