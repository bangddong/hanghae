package io.hhplus.tdd.point.service.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;

class PointManagerTest {

	@Mock
	private UserPointTable userPointTable;

	@InjectMocks
	private PointManager pointManager;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private final long MAX_AMOUNT = 1000000;
	private final long MIN_AMOUNT = 1000;

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
	void 포인트_충전_최소포인트_미달_테스트() {
		// Given
		long userId = 1L;
		long initialPoints = 500L;
		long chargeAmount = 999L;

		UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());

		when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);

		// When & Then
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> pointManager.charge(userId, chargeAmount));

		verify(userPointTable, times(1)).selectById(userId);
		verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());

		assertEquals("최소 충전 포인트는 " + MIN_AMOUNT + "포인트 입니다.", exception.getMessage());
	}

	@Test
	void 포인트_충전_최대포인트_초과_테스트() {
		// Given
		long userId = 1L;
		long initialPoints = 500L;
		long chargeAmount = 1000001L;

		UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());

		when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);

		// When & Then
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> pointManager.charge(userId, chargeAmount));

		verify(userPointTable, times(1)).selectById(userId);
		verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());

		assertEquals("1회 최대 충전 포인트는 " + MAX_AMOUNT + "포인트 입니다.", exception.getMessage());
	}

	@Test
	void 포인트_사용_테스트() {
		// Given
		long userId = 1L;
		long initialPoints = 1001L;
		long useAmount = 1000L;

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
	void 포인트_사용_최소포인트_미달_테스트() {
		// Given
		long userId = 1L;
		long initialPoints = 1000L;
		long useAmount = 900L;

		UserPoint currentUserPoint = new UserPoint(userId, initialPoints, System.currentTimeMillis());

		when(userPointTable.selectById(userId)).thenReturn(currentUserPoint);

		// When & Then
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> pointManager.use(userId, useAmount));

		verify(userPointTable, times(1)).selectById(userId);
		verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());

		assertEquals("최소 사용 포인트는 " + MIN_AMOUNT + "포인트 입니다.", exception.getMessage());
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