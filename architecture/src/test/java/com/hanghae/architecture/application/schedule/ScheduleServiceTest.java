package com.hanghae.architecture.application.schedule;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import com.hanghae.architecture.interfaces.dto.ScheduleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void 예약_가능한_스케줄_조회() {
        // Given
        Lecture lecture1 = Lecture.of("이석범", Subject.JAVA);
        Lecture lecture2 = Lecture.of("렌", Subject.SPRING);
        Schedule schedule1 = Schedule.of(lecture1, LocalDate.of(2024, 10, 22), 10);
        Schedule schedule2 = Schedule.of(lecture2, LocalDate.of(2024, 10, 21), 20);
        ReflectionTestUtils.setField(schedule1, "id", 1L);
        ReflectionTestUtils.setField(schedule2, "id", 2L);

        List<Schedule> schedules = List.of(schedule1, schedule2);

        when(scheduleRepository.getScheduleList()).thenReturn(schedules);

        // When
        List<ScheduleResponse> scheduleResponses = scheduleService.getSchedule();

        // Then
        assertEquals(2, scheduleResponses.size());
        assertEquals("2024-10-22", scheduleResponses.get(0).date());
        assertEquals(10, scheduleResponses.get(0).count());
        assertEquals("이석범", scheduleResponses.get(0).lecture().tutor());
        assertEquals(Subject.JAVA, scheduleResponses.get(0).lecture().subject());

        assertEquals("2024-10-21", scheduleResponses.get(1).date());
        assertEquals(20, scheduleResponses.get(1).count());
        assertEquals("렌", scheduleResponses.get(1).lecture().tutor());
        assertEquals(Subject.SPRING, scheduleResponses.get(1).lecture().subject());

        verify(scheduleRepository, times(1)).getScheduleList();
    }

}