package com.hanghae.architecture.infrastructure.repository;

import com.hanghae.architecture.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select s from Schedule s where s.count < s.capacity and s.date > CURRENT_DATE")
    List<Schedule> findAvailableSchedule();

    @Modifying
    @Transactional
    @Query("UPDATE Schedule s SET s.count = s.count + 1 WHERE s.id = :scheduleId")
    void incrementCount(long scheduleId);

    @Query("SELECT s.count FROM Schedule s WHERE s.id = :scheduleId")
    long countByScheduleId(long scheduleId);
}
