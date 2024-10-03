package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.schedule.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationRepositoryImplTest {

    @Mock
    private JpaReservationRepository jpaReservationRepository;

    @InjectMocks
    private ReservationRepositoryImpl reservationRepositoryImpl;

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
        List<Reservation> result = reservationRepositoryImpl.findByUserId(1L);

        // Then
        assertEquals(1, result.size());
        verify(jpaReservationRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testExistsByUserIdAndLecture() {
        // Given
        when(jpaReservationRepository.existsByUserIdAndScheduleId(1L, 2L)).thenReturn(true);

        // When
        boolean result = reservationRepositoryImpl.existsByUserIdAndLecture(1L, 2L);

        // Then
        assertEquals(true, result);
        verify(jpaReservationRepository, times(1)).existsByUserIdAndScheduleId(1L, 2L);
    }

    @Test
    public void testSave() {
        // Given
        Lecture lecture = Lecture.of("이석범", Subject.JAVA);
        Schedule schedule = Schedule.of(lecture, LocalDate.of(2024, 10, 3), 20);
        Reservation reservation = Reservation.of(1L, schedule);

        // When
        reservationRepositoryImpl.save(reservation);

        // Then
        verify(jpaReservationRepository, times(1)).save(reservation);
    }
}