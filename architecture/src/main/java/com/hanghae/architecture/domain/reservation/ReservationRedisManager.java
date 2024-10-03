package com.hanghae.architecture.domain.reservation;

public interface ReservationRedisManager {
    long incrementCount(long scheduleId);
    long decrementCount(long scheduleId);
}
