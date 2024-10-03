package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.infrastructure.repository.JpaScheduleRepository;
import com.hanghae.architecture.infrastructure.repository.ScheduleRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScheduleRepositoryJpaAdapterTest {

    @Mock
    private JpaScheduleRepository jpaScheduleRepository;

    @InjectMocks
    private ScheduleRepositoryJpaAdapter scheduleRepositoryJpaAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetScheduleList() {
        // Given
        Schedule schedule = new Schedule();
        when(jpaScheduleRepository.findAvailableSchedule()).thenReturn(List.of(schedule));

        // When
        List<Schedule> result = scheduleRepositoryJpaAdapter.getScheduleList();

        // Then
        assertEquals(1, result.size());
        verify(jpaScheduleRepository, times(1)).findAvailableSchedule();
    }

    @Test
    public void testFindById() {
        // Given
        Schedule schedule = new Schedule();
        when(jpaScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        // When
        Optional<Schedule> result = scheduleRepositoryJpaAdapter.findById(1L);

        // Then
        assertEquals(true, result.isPresent());
        verify(jpaScheduleRepository, times(1)).findById(1L);
    }

}