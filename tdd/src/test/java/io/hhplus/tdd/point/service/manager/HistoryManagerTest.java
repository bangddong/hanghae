package io.hhplus.tdd.point.service.manager;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class HistoryManagerTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 히스토리_기록_테스트() {
        // Given
        long userId = 1L;
        long amount = 1000L;
        TransactionType transactionType = TransactionType.CHARGE;
        long timestamp = System.currentTimeMillis();

        // When
        historyManager.append(userId, amount, transactionType, timestamp);

        // Then
        verify(pointHistoryTable, times(1)).insert(userId, amount, transactionType, timestamp);
    }
}
