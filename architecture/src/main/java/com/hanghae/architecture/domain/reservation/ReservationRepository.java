package com.hanghae.architecture.domain.reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByUserId(long userId);

    boolean existsByUserIdAndLecture(long userId, long scheduleId);

    void save(Reservation of);
}
