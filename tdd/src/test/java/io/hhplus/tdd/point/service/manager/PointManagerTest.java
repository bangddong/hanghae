package io.hhplus.tdd.point.service.manager;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class PointManagerTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointManager pointManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 포인트_충전_테스트() {
        // Given
        long userId = 1L;
        long initialPoints = 500L;
        long chargeAmount = 1000L;

        UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, initialPoints + chargeAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(userId, initialPoints + chargeAmount)).thenReturn(updatedUserPoint);

        // When
        UserPoint result = pointManager.charge(userId, chargeAmount);

        // Then
        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1)).insertOrUpdate(userId, initialPoints + chargeAmount);

        assertEquals(initialPoints + chargeAmount, result.point());
    }

    @Test
    void 포인트_사용_테스트() {
        // Given
        long userId = 1L;
        long initialPoints = 1000L;
        long useAmount = 500L;

        UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, initialPoints - useAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);
        when(userPointTable.insertOrUpdate(userId, initialPoints - useAmount)).thenReturn(updatedUserPoint);

        // When
        UserPoint result = pointManager.use(userId, useAmount);

        // Then
        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1)).insertOrUpdate(userId, initialPoints - useAmount);

        assertEquals(initialPoints - useAmount, result.point());
    }

    @Test
    void 포인트_사용_잔액부족_테스트() {
        // Given
        long userId = 1L;
        long initialPoints = 500L;
        long useAmount = 1000L;

        UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> pointManager.use(userId, useAmount));

        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());

        assertEquals("잔액이 부족합니다.", exception.getMessage());
    }
}