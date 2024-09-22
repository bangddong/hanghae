package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private PointService pointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 유저_포인트_조회() {
        // Given
        long userId = 1L;
        UserPoint userPoint = UserPoint.empty(userId);
        when(pointService.getPoint(userId)).thenReturn(userPoint);

        // When
        UserPoint result = pointService.getPoint(userId);

        // Then
        assertEquals(userId, result.id());
        assertEquals(0, result.point());
        verify(userPointTable, times(1)).selectById(userId);
    }

    @Test
    void 유저_포인트_내역_조회() {
        // Given
        long userId = 1L;
        long chargeAmount = 1000L;
        long useAmount = 100L;
        List<PointHistory> pointHistories = Arrays.asList(
                new PointHistory(1L, userId, chargeAmount, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, useAmount, TransactionType.USE, System.currentTimeMillis())
        );
        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(pointHistories);

        // When
        List<PointHistory> result = pointService.getHistories(userId);

        // Then
        assertEquals(pointHistories.size(), result.size());
        assertEquals(chargeAmount, result.get(0).amount());
        assertEquals(TransactionType.CHARGE, result.get(0).type());
        assertEquals(useAmount, result.get(1).amount());
        assertEquals(TransactionType.USE, result.get(1).type());

        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
    }

    @Test
    void 유저_포인트_충전() {
        // Given
        long userId = 1L;
        long initialPoints = 1000L;
        long chargeAmount = 1000L;

        UserPoint initialUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, initialPoints + chargeAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId))
                .thenReturn(initialUserPoint)
                .thenReturn(updatedUserPoint);

        // When
        UserPoint result = pointService.charge(userId, chargeAmount);

        // Then
        assertEquals(initialPoints + chargeAmount, result.point());
        verify(userPointTable, times(1))
                .insertOrUpdate(eq(userId), eq(initialPoints + chargeAmount));
        verify(pointHistoryTable, times(1))
                .insert(eq(userId), eq(chargeAmount), eq(TransactionType.CHARGE), anyLong());
    }

    @Test
    void 유저_포인트_사용_성공() {
        // Given
        long userId = 1L;
        long initialPoints = 1000L;
        long useAmount = 500L;

        UserPoint initialUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());
        UserPoint updatedUserPoint = new UserPoint(userId, initialPoints - useAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(initialUserPoint);
        when(userPointTable.insertOrUpdate(userId, useAmount)).thenReturn(updatedUserPoint);

        // When
        UserPoint result = pointService.use(userId, useAmount);

        // Then
        assertEquals(initialPoints - useAmount, result.point());
        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1))
                .insertOrUpdate(eq(userId), eq(initialPoints - useAmount));
        verify(pointHistoryTable, times(1))
                .insert(eq(userId), eq(useAmount), eq(TransactionType.USE), anyLong());
    }

    @Test
    void 유저_포인트_사용_잔액부족() {
        // Given
        long userId = 1L;
        long initialPoints = 1000L;
        long useAmount = 1500L;

        UserPoint userPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // When & Then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> pointService.use(userId, useAmount));

        assertEquals("잔액이 부족합니다.", exception.getMessage());
        verify(userPointTable).selectById(userId);
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());
        verify(pointHistoryTable, never()).insert(anyLong(), anyLong(), any(), anyLong());
    }
}
