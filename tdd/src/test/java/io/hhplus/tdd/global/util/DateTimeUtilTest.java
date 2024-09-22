package io.hhplus.tdd.global.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateTimeUtilTest {

    @Test
    void 타임스탬프_정상_변환() {
        // Given
        Long timestamp = 1726977600000L;

        // When
        String formattedDate = DateTimeUtil.convertTimeMiles(timestamp);

        // Then
        assertEquals("2024-09-22 13:00:00", formattedDate);
    }

    @Test
    void 타임스탬프_null_처리() {
        // Given
        Long timestamp = null;

        // When
        String result = DateTimeUtil.convertTimeMiles(timestamp);

        // Then
        assertNull(result);
    }

}
