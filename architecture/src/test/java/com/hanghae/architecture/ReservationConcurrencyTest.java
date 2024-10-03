package com.hanghae.architecture;

import com.hanghae.architecture.application.reservation.ReservationService;
import com.hanghae.architecture.domain.reservation.ReservationRepository;
import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import com.hanghae.architecture.interfaces.dto.ReserveRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    public void 특강신청_동시성_테스트() throws InterruptedException {
        long scheduleId = 1L;
        int totalRequests = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(totalRequests);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        for (int i = 1; i <= totalRequests; i++) {
            final long userId = i;
            executorService.submit(() -> {
                try {
                    reservationService.reserve(userId, new ReserveRequest(scheduleId));
                    successfulReservations.incrementAndGet();
                } catch (Exception e) {
                    failedReservations.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(30, successfulReservations.get());
        assertEquals(10, failedReservations.get());

        long scheduleCurrentCount = scheduleRepository.getCountByScheduleId(scheduleId);
        assertEquals(30, scheduleCurrentCount);

        long reservationCount = reservationRepository.countByScheduleId(scheduleId);
        assertEquals(30, reservationCount);
    }

    @Test
    public void 같은_유저_동시성_테스트() throws InterruptedException {
        long scheduleId = 2L;
        long userId = 0L;
        int totalRequests = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(totalRequests);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        AtomicInteger successfulReservations = new AtomicInteger(0);
        AtomicInteger failedReservations = new AtomicInteger(0);

        for (int i = 0; i < totalRequests; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.reserve(userId, new ReserveRequest(scheduleId));
                    successfulReservations.incrementAndGet();
                } catch (Exception e) {
                    failedReservations.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertEquals(1, successfulReservations.get());
        assertEquals(4, failedReservations.get());

        long reservationCount = reservationRepository.findByUserId(userId).size();
        assertEquals(1, reservationCount);
    }
}
