package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.infrastructure.repository.JpaReservationRepository;
import com.hanghae.architecture.infrastructure.repository.ReservationRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationRepositoryJpaAdapterTest {

    @Mock
    private JpaReservationRepository jpaReservationRepository;

    @InjectMocks
    private ReservationRepositoryJpaAdapter reservationRepositoryJpaAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByUserId() {
        // Given
        Lecture lecture = Lecture.of("이석범", Subject.JAVA);
        Schedule schedule = Schedule.of(lecture, LocalDate.of(2024, 10, 3), 20);
        Reservation reservation = Reservation.of(1L, schedule);
        when(jpaReservationRepository.findByUserId(1L)).thenReturn(List.of(reservation));

        // When
        List<Reservation> result = reservationRepositoryJpaAdapter.findByUserId(1L);

        // Then
        assertEquals(1, result.size());
        verify(jpaReservationRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testSave() {
        // Given
        Lecture lecture = Lecture.of("이석범", Subject.JAVA);
        Schedule schedule = Schedule.of(lecture, LocalDate.of(2024, 10, 3), 20);
        Reservation reservation = Reservation.of(1L, schedule);

        // When
        reservationRepositoryJpaAdapter.save(reservation);

        // Then
        verify(jpaReservationRepository, times(1)).save(reservation);
    }
}