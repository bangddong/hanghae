package io.hhplus.tdd.point.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.manager.HistoryManager;
import io.hhplus.tdd.point.service.manager.PointManager;
import io.hhplus.tdd.point.service.reader.HistoryReader;
import io.hhplus.tdd.point.service.reader.PointReader;
import io.hhplus.tdd.point.type.TransactionType;

class PointServiceTest {

	@Mock
	private PointReader pointReader;

	@Mock
	private HistoryReader historyReader;

	@Mock
	private PointManager pointManager;

	@Mock
	private HistoryManager historyManager;

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

		UserPoint userPoint = new UserPoint(userId, 0L, System.currentTimeMillis());

		when(pointReader.read(userId)).thenReturn(userPoint);

		// When
		UserPointResponse result = pointService.getPoint(userId);

		// Then
		verify(pointReader, times(1)).read(userId);

		assertEquals(userId, result.id());
		assertEquals(0, result.point());
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

		when(historyReader.read(userId)).thenReturn(pointHistories);

		// When
		List<PointHistoryResponse> result = pointService.getHistories(userId);

		// Then
		verify(historyReader, times(1)).read(userId);

		assertEquals(pointHistories.size(), result.size());
		assertEquals(chargeAmount, result.get(0).amount());
		assertEquals("CHARGE", result.get(0).type());
		assertEquals(useAmount, result.get(1).amount());
		assertEquals("USE", result.get(1).type());
	}

	@Test
	void 유저_포인트_충전() {
		// Given
		long userId = 1L;
		long chargeAmount = 1000L;

		UserPoint updatedUserPoint = new UserPoint(userId, chargeAmount, System.currentTimeMillis());

		when(pointManager.charge(userId, chargeAmount))
			.thenReturn(updatedUserPoint);

		// When
		UserPointResponse result = pointService.charge(userId, chargeAmount);

		// Then
		verify(pointManager, times(1))
			.charge(eq(userId), eq(chargeAmount));
		verify(historyManager, times(1))
			.append(eq(userId), eq(chargeAmount), eq(TransactionType.CHARGE), anyLong());

		assertEquals(chargeAmount, result.point());
	}

	@Test
	void 유저_포인트_사용_성공() {
		// Given
		long userId = 1L;
		long useAmount = 500L;

		UserPoint updatedUserPoint = new UserPoint(userId, useAmount, System.currentTimeMillis());

		when(pointManager.use(userId, useAmount)).thenReturn(updatedUserPoint);

		// When
		UserPointResponse result = pointService.use(userId, useAmount);

		// Then
		verify(pointManager, times(1))
			.use(eq(userId), eq(useAmount));
		verify(historyManager, times(1))
			.append(eq(userId), eq(useAmount), eq(TransactionType.USE), anyLong());

		assertEquals(useAmount, result.point());
	}

	@Test
	void 유저_포인트_사용_잔액부족() {
		// Given
		long userId = 1L;
		long useAmount = 1500L;

		when(pointManager.use(userId, useAmount)).thenThrow(new IllegalArgumentException("잔액이 부족합니다."));

		// When & Then
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> pointService.use(userId, useAmount));

		verify(pointManager, times(1)).use(eq(userId), eq(useAmount));
		verify(historyManager, never()).append(anyLong(), anyLong(), any(), anyLong());

		assertEquals("잔액이 부족합니다.", exception.getMessage());
	}

}
