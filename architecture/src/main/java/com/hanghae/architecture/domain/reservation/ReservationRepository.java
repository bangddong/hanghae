package com.hanghae.architecture.domain.reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByUserId(long userId);
    void save(Reservation of);
    long countByScheduleId(Long id);
}
