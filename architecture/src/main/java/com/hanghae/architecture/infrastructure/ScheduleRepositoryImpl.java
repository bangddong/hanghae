package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final JpaScheduleRepository jpaScheduleRepository;

    @Override
    public List<Schedule> getScheduleList() {
        return jpaScheduleRepository.findAvailableSchedule();
    }

    @Override
    public Optional<Schedule> findById(long scheduleId) {
        return jpaScheduleRepository.findById(scheduleId);
    }
}
