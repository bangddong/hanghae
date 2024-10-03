package com.hanghae.architecture.domain.schedule;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    private Lecture lecture;
    private Schedule schedule;

    @BeforeEach
    public void setUp() {
        lecture = Lecture.of("이석범", Subject.JAVA);
    }

    @Test
    public void 수강신청() {
        // Given
        schedule = Schedule.of(lecture, LocalDate.now(), 10);

        // When
        boolean result = schedule.canReserve();

        // Then
        assertTrue(result);
    }

    @Test
    public void 수강신청_실패_자리초과() {
        // Given
        schedule = Schedule.of(lecture, LocalDate.now(), 30);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            schedule.reserve();
        });

        assertEquals("수강 인원이 초과되었습니다.", exception.getMessage());
    }

}