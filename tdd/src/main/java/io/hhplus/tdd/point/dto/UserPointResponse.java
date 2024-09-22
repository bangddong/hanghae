package io.hhplus.tdd.point.dto;

import io.hhplus.tdd.point.entity.UserPoint;

import static io.hhplus.tdd.global.util.DateTimeUtil.convertTimeMiles;

public record UserPointResponse(
        long id,
        long point,
        String updatedAt
) {
    public static UserPointResponse fromEntity(UserPoint userPoint) {
        return new UserPointResponse(userPoint.id(), userPoint.point(), convertTimeMiles(userPoint.updateMillis()));
    }
}
