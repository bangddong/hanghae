package com.hanghae.architecture.interfaces.api;

import com.hanghae.architecture.application.reservation.ReservationService;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.interfaces.dto.ReservationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;

    private final long userId = 1L;

    @Test
    void 특강_신청내역_조회() throws Exception{
        // Given
        List<ReservationResponse> reservationResponses = List.of(
                new ReservationResponse(1L, "이석범", Subject.JAVA, "2024-10-22"),
                new ReservationResponse(2L, "렌", Subject.SPRING, "2024-10-22")
        );
        when(reservationService.getReservations(userId)).thenReturn(reservationResponses);

        // When & Then
        mvc.perform(
                get("/reservation/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tutor").value("이석범"))
                .andExpect(jsonPath("$[0].subject").value("JAVA"))
                .andExpect(jsonPath("$[0].date").value("2024-10-22"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].tutor").value("렌"))
                .andExpect(jsonPath("$[1].subject").value("SPRING"))
                .andExpect(jsonPath("$[1].date").value("2024-10-22"));

    }

    @Test
    void 특강신청() throws Exception{
        // Given

        // When & Then
        mvc.perform(
                post("/reservation/1")
                        .content("{\"lectureId\":1}")
                        .contentType("application/json")
        )
                .andExpect(status().isOk());

    }
}