package io.hhplus.tdd.point.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.type.TransactionType;

@SpringBootTest
public class PointServiceIntegrationTest {

	@Autowired
	private PointService pointService;

	private final long initialAmount = 10000L;
	private final long userId = 1L;

	@BeforeEach
	void setUp() throws Exception {
		pointService.charge(userId, initialAmount);
	}

	@Test
	void 충전_사용_동시성_테스트() throws Exception {
		// Given
		int threadCount = 5;
		long chargePoint = 1500L;
		long usePoint = 1100L;

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount * 2);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount * 2);

		// When
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					pointService.charge(userId, chargePoint);
				} finally {
					countDownLatch.countDown();
				}
			});

			executorService.submit(() -> {
				try {
					pointService.use(userId, usePoint);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		// 모든 스레드가 완료될 때까지 대기
		countDownLatch.await(10, TimeUnit.SECONDS);
		executorService.shutdown();

		// Then
		UserPointResponse finalPoint = pointService.getPoint(userId);
		long expectedFinalAmount = initialAmount + (chargePoint * threadCount) - (usePoint * threadCount);
		assertEquals(expectedFinalAmount, finalPoint.point());

		List<PointHistoryResponse> histories = pointService.getHistories(userId);
		assertEquals(threadCount * 2 + 1, histories.size());

		long chargeHistoryCount = histories.stream()
			.filter(history -> history.type().equals(TransactionType.CHARGE.name()))
			.count();
		assertEquals(threadCount + 1, chargeHistoryCount);

		long useHistoryCount = histories.stream()
			.filter(history -> history.type().equals(TransactionType.USE.name()))
			.count();
		assertEquals(threadCount, useHistoryCount);
	}
}
