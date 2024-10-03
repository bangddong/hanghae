package com.hanghae.architecture.infrastructure.redis;

import com.hanghae.architecture.domain.reservation.ReservationRedisManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisAdapter implements ReservationRedisManager {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public long incrementCount(long scheduleId) {
        String key = "schedule:" + scheduleId;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.increment(key);
    }

    @Override
    public long decrementCount(long scheduleId) {
        String key = "schedule:" + scheduleId;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.decrement(key);
    }

}
