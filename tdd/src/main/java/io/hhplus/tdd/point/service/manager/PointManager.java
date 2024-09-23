package io.hhplus.tdd.point.service.manager;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import lombok.RequiredArgsConstructor;

/**
 * TODO
 * 1. 포인트 충전, 사용에 대한 정책 추가 (잔고부족, 최대 포인트 등) (O)
 * 2. 동시성 제어
 * 3. 통합테스트 작성
 */
@Component
@RequiredArgsConstructor
public class PointManager {

	private final UserPointTable userPointTable;

	private final long MAX_AMOUNT = 1000000;
	private final long MIN_AMOUNT = 1000;

	public UserPoint charge(long id, long amount) {
		final UserPoint userPoint = userPointTable.selectById(id);

		if (amount < MIN_AMOUNT) {
			throw new IllegalArgumentException("최소 충전 포인트는 " + MIN_AMOUNT + "포인트 입니다.");
		}
		if (amount > MAX_AMOUNT) {
			throw new IllegalArgumentException("1회 최대 충전 포인트는 " + MAX_AMOUNT + "포인트 입니다.");
		}

		return userPointTable.insertOrUpdate(id, userPoint.point() + amount);
	}

	public UserPoint use(long id, long amount) {
		final UserPoint userPoint = userPointTable.selectById(id);

		if (amount < MIN_AMOUNT) {
			throw new IllegalArgumentException("최소 사용 포인트는 " + MIN_AMOUNT + "포인트 입니다.");
		}
		if (userPoint.point() < amount) {
			throw new IllegalArgumentException("잔액이 부족합니다.");
		}

		return userPointTable.insertOrUpdate(id, userPoint.point() - amount);
	}

}
