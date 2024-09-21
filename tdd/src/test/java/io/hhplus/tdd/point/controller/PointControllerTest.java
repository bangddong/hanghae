package io.hhplus.tdd.point.controller;

import io.hhplus.tdd.point.dto.PointHistory;
import io.hhplus.tdd.point.dto.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.type.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PointService pointService;

    @Test
    void 유저_포인트_조회() throws Exception {
        // given
        long userId = 1L;
        UserPoint userPoint = UserPoint.empty(userId);
        when(pointService.getPoint(userId)).thenReturn(userPoint);

        // when & then
        mvc.perform(
                get("/point/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0L))
                .andExpect(jsonPath("$.updateMillis").exists())
                .andDo(print());
    }

    @Test
    void 유저_포인트_내역_조회() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;
        long useAmount = 100L;

        List<PointHistory> histories = List.of(
                new PointHistory(1L, userId, chargeAmount, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2L, userId, useAmount, TransactionType.USE, System.currentTimeMillis())
        );
        when(pointService.getHistories(userId)).thenReturn(histories);

        // when & then
        mvc.perform(
                        get("/point/1/histories")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].amount").value(chargeAmount))
                .andExpect(jsonPath("$[0].type").value(TransactionType.CHARGE.name()))
                .andExpect(jsonPath("$[0].updateMillis").exists())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(userId))
                .andExpect(jsonPath("$[1].amount").value(useAmount))
                .andExpect(jsonPath("$[1].type").value(TransactionType.USE.name()))
                .andExpect(jsonPath("$[1].updateMillis").exists())
                .andDo(print());
    }

    @Test
    void 유저_포인트_충전() throws Exception{
        // given
        long userId = 1L;
        long chargeAmount = 1000L;
        UserPoint userPoint = new UserPoint(userId, chargeAmount, System.currentTimeMillis());
        when(pointService.charge(userId, chargeAmount)).thenReturn(userPoint);

        // when & then
        mvc.perform(
                        patch("/point/1/charge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("1000")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(chargeAmount))
                .andExpect(jsonPath("$.updateMillis").exists())
                .andDo(print());
    }

    @Test
    void 유저_포인트_사용_성공() throws Exception {
        // given
        long userId = 1L;
        long useAmount = 100L;
        UserPoint userPoint = UserPoint.empty(userId);
        when(pointService.use(userId, useAmount)).thenReturn(userPoint);

        // when & then
        mvc.perform(
                        patch("/point/1/use")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("100")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0))
                .andExpect(jsonPath("$.updateMillis").exists())
                .andDo(print());
    }

    @Test
    void 유저_포인트_사용_잔액부족() throws Exception {
        // given
        long userId = 1L;
        long useAmount = 1000L;
        doThrow(new IllegalArgumentException("잔액이 부족합니다."))
                .when(pointService).use(userId, useAmount);

        // when & then
        mvc.perform(
                        patch("/point/1/use")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("1000")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.message").value("잔액이 부족합니다."))
                .andDo(print());
    }
}