package io.hhplus.tdd.point.service.reader;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.entity.PointHistory;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HistoryReaderTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private HistoryReader historyReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 히스토리_조회_테스트() {
        // Given
        long userId = 1L;
        List<PointHistory> pointHistories = Arrays.asList(
                new PointHistory(1L, userId, 1000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, 500L, TransactionType.USE, System.currentTimeMillis())
        );

        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(pointHistories);

        // When
        List<PointHistory> result = historyReader.read(userId);

        // Then
        assertEquals(2, result.size());
        assertEquals(1000L, result.get(0).amount());
        assertEquals(TransactionType.CHARGE, result.get(0).type());
        assertEquals(500L, result.get(1).amount());
        assertEquals(TransactionType.USE, result.get(1).type());

        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
    }
}
