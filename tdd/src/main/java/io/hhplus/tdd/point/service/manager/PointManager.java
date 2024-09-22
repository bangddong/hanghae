package io.hhplus.tdd.point.service.manager;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointManager {

    private final UserPointTable userPointTable;

    public UserPoint charge(long id, long amount) {
        final UserPoint userPoint = userPointTable.selectById(id);
        return userPointTable.insertOrUpdate(id, userPoint.point() + amount);
    }

    public UserPoint use(long id, long amount) {
        final UserPoint userPoint = userPointTable.selectById(id);
        if (userPoint.point() < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        return userPointTable.insertOrUpdate(id, userPoint.point() - amount);
    }

}
