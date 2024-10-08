package io.hhplus.tdd.point.service.manager;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.type.TransactionType;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HistoryManager {

	private final PointHistoryTable pointHistoryTable;

	public void append(long id, long amount, TransactionType transactionType, long timestamp) {
		pointHistoryTable.insert(id, amount, transactionType, timestamp);
	}

}
