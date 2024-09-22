package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.entity.PointHistory;

import static io.hhplus.tdd.global.util.DateTimeUtil.convertTimeMiles;

public record PointHistoryResponse(
        long id,
        long userId,
        long amount,
        String type,
        String updatedAt
) {
    public static PointHistoryResponse fromEntity(PointHistory pointHistory) {
        return new PointHistoryResponse(
                pointHistory.id(),
                pointHistory.userId(),
                pointHistory.amount(),
                pointHistory.type().name(),
                convertTimeMiles(pointHistory.updateMillis())
        );
    }
}
