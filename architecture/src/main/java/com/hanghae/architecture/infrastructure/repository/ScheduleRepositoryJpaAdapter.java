package com.hanghae.architecture.infrastructure.repository;

import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryJpaAdapter implements ScheduleRepository {
    private final JpaScheduleRepository jpaScheduleRepository;

    @Override
    public List<Schedule> getScheduleList() {
        return jpaScheduleRepository.findAvailableSchedule();
    }

    @Override
    public Optional<Schedule> findById(long scheduleId) {
        return jpaScheduleRepository.findById(scheduleId);
    }

    @Override
    public void incrementCount(long scheduleId) {
        jpaScheduleRepository.incrementCount(scheduleId);
    }

    @Override
    public long getCountByScheduleId(Long id) {
        return jpaScheduleRepository.countByScheduleId(id);
    }

    @Override
    public void save(Schedule schedule) {
        jpaScheduleRepository.save(schedule);
    }
}
