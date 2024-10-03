package com.hanghae.architecture.application.reservation;

import com.hanghae.architecture.domain.reservation.Reservation;
import com.hanghae.architecture.domain.reservation.ReservationRedisManager;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import com.hanghae.architecture.domain.schedule.Schedule;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import com.hanghae.architecture.interfaces.dto.ReservationResponse;
import com.hanghae.architecture.interfaces.dto.ReserveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    ReservationRedisManager reservationRedisManager;

    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final long userId = 1L;
    private final long scheduleId = 1L;

    @Test
    public void 신청내역_조회() {
        // Given
        Reservation reservation = mock(Reservation.class);
        ReservationResponse reservationResponse = mock(ReservationResponse.class);

        when(reservationRepository.findByUserId(userId)).thenReturn(List.of(reservation));

        try (MockedStatic<ReservationResponse> mockedStatic = mockStatic(ReservationResponse.class)) {
            mockedStatic.when(() -> ReservationResponse.fromEntity(reservation)).thenReturn(reservationResponse);

            // When
            List<ReservationResponse> result = reservationService.getReservations(userId);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(reservationRepository, times(1)).findByUserId(userId);
        }
    }

    @Test
    public void 예약() {
        // Given
        ReserveRequest reserveRequest = mock(ReserveRequest.class);
        when(reserveRequest.scheduleId()).thenReturn(scheduleId);

        Schedule schedule = mock(Schedule.class);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        when(reservationRedisManager.reserveOrAlready(userId, scheduleId)).thenReturn(true);

        // When
        reservationService.reserve(userId, reserveRequest);

        // Then
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    public void 예약실패_이미_예약된_강의() {
        // Given
        ReserveRequest reserveRequest = mock(ReserveRequest.class);
        when(reserveRequest.scheduleId()).thenReturn(scheduleId);

        Schedule schedule = mock(Schedule.class);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        when(reservationRedisManager.reserveOrAlready(userId, scheduleId)).thenReturn(false);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.reserve(userId, reserveRequest);
        });
        assertEquals("이미 예약 된 강의입니다.", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void 예약실패_존재하지_않는_강의() {
        // Given
        ReserveRequest reserveRequest = mock(ReserveRequest.class);
        when(reserveRequest.scheduleId()).thenReturn(scheduleId);

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.reserve(userId, reserveRequest);
        });
        assertEquals("강의가 존재하지 않습니다.", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void 예약실패_수강인원_초과() {
        // Given
        ReserveRequest reserveRequest = mock(ReserveRequest.class);
        when(reserveRequest.scheduleId()).thenReturn(scheduleId);

        Schedule schedule = mock(Schedule.class);
        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

        when(reservationRedisManager.reserveOrAlready(userId, scheduleId)).thenReturn(true);
        when(reservationRedisManager.incrementCount(scheduleId)).thenReturn(31L);

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.reserve(userId, reserveRequest);
        });
        assertEquals("수강 인원이 초과되었습니다.", exception.getMessage());
        verify(reservationRepository, never()).save(any(Reservation.class));
    }
}