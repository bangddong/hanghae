package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(long userId);
    boolean existsByUserIdAndScheduleId(long userId, long scheduleId);
}
