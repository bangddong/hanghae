package com.hanghae.architecture.interfaces.api;

import com.hanghae.architecture.application.schedule.ScheduleService;
import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.interfaces.dto.LectureResponse;
import com.hanghae.architecture.interfaces.dto.ScheduleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleApiController.class)
class ScheduleApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService scheduleService;

    @Test
    void 신청_가능한_스케줄_조회() throws Exception{
        // Given
        Lecture lecture1 = Lecture.of("이석범", Subject.JAVA);
        Lecture lecture2 = Lecture.of("렌", Subject.SPRING);
        List<ScheduleResponse> reservationResponses = List.of(
                new ScheduleResponse(1L, LectureResponse.fromEntity(lecture1), "2024-10-22", 10),
                new ScheduleResponse(2L, LectureResponse.fromEntity(lecture2), "2024-10-22", 10)
        );
        when(scheduleService.getSchedule()).thenReturn(reservationResponses);

        // When & Then
        mvc.perform(
                        get("/schedule")
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}