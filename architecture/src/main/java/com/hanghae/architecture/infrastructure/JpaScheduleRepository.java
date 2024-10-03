package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s where s.count < s.capacity")
    List<Schedule> findAvailableSchedule();
}
