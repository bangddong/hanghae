package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.dto.PointHistoryResponse;
import io.hhplus.tdd.point.dto.UserPointResponse;
import io.hhplus.tdd.point.entity.UserPoint;
import io.hhplus.tdd.point.service.manager.HistoryManager;
import io.hhplus.tdd.point.service.manager.PointManager;
import io.hhplus.tdd.point.service.reader.HistoryReader;
import io.hhplus.tdd.point.service.reader.PointReader;
import io.hhplus.tdd.point.type.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointReader pointReader;
    private final HistoryReader historyReader;
    private final PointManager pointManager;
    private final HistoryManager historyManager;

    public UserPointResponse getPoint(long id) {
        return UserPointResponse.fromEntity(pointReader.read(id));
    }

    public List<PointHistoryResponse> getHistories(long id) {
        return historyReader.read(id).stream()
                .map(PointHistoryResponse::fromEntity)
                .toList();
    }

    public UserPointResponse charge(long id, long amount) {
        final UserPoint updatedUserPoint = pointManager.charge(id, amount);
        historyManager.append(id, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return UserPointResponse.fromEntity(updatedUserPoint);
    }

    public UserPointResponse use(long id, long amount) {
        final UserPoint updatedUserPoint = pointManager.use(id, amount);
        historyManager.append(id, amount, TransactionType.USE, System.currentTimeMillis());
        return UserPointResponse.fromEntity(updatedUserPoint);
    }
}
