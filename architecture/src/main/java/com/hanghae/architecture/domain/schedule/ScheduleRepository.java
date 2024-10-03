package com.hanghae.architecture.domain.schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    List<Schedule> getScheduleList();
    Optional<Schedule> findById(long scheduleId);
}
