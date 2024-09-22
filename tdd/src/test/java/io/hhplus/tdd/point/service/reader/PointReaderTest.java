package io.hhplus.tdd.point.service.reader;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.entity.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PointReaderTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointReader pointReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 포인트_조회_테스트() {
        // Given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 1000L, System.currentTimeMillis());

        // Mock 설정
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // When
        UserPoint result = pointReader.read(userId);

        // Then
        assertEquals(userId, result.id());
        assertEquals(1000L, result.point());

        verify(userPointTable, times(1)).selectById(userId);
    }
}