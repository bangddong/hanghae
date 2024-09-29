package io.hhplus.tdd.point.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.manager.HistoryManager;
import io.hhplus.tdd.point.service.manager.PointManager;
import io.hhplus.tdd.point.service.reader.HistoryReader;
import io.hhplus.tdd.point.service.reader.PointReader;
import io.hhplus.tdd.point.type.TransactionType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointReader pointReader;
	private final HistoryReader historyReader;
	private final PointManager pointManager;
	private final HistoryManager historyManager;

	private final ConcurrentHashMap<Long, ReentrantLock> userLocks = new ConcurrentHashMap<>();

	private ReentrantLock getUserLock(long userId) {
		return userLocks.computeIfAbsent(userId, key -> new ReentrantLock());
	}

	public UserPointResponse getPoint(long id) {
		return UserPointResponse.fromEntity(pointReader.read(id));
	}

	public List<PointHistoryResponse> getHistories(long id) {
		return historyReader.read(id).stream()
				.map(PointHistoryResponse::fromEntity)
				.toList();
	}

	public UserPointResponse charge(long id, long amount) {
		final UserPoint updatedUserPoint;

		ReentrantLock lock = getUserLock(id);
		lock.lock();
		try {
			updatedUserPoint = pointManager.charge(id, amount);
		} finally {
			lock.unlock();
		}

		historyManager.append(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
		return UserPointResponse.fromEntity(updatedUserPoint);
	}

	public UserPointResponse use(long id, long amount) {
		final UserPoint updatedUserPoint;

		ReentrantLock lock = getUserLock(id);
		lock.lock();
		try {
			updatedUserPoint = pointManager.use(id, amount);
		} finally {
			lock.unlock();
		}

		historyManager.append(id, amount, TransactionType.USE, System.currentTimeMillis());
		return UserPointResponse.fromEntity(updatedUserPoint);
	}

}
