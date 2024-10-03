package com.hanghae.architecture.interfaces.dto;

import com.hanghae.architecture.domain.schedule.Schedule;

public record ScheduleResponse(
    long  id,
    LectureResponse lecture,
    String date,
    int count
) {
    public static ScheduleResponse fromEntity(Schedule schedule) {
        return new ScheduleResponse(
            schedule.getId(),
            LectureResponse.fromEntity(schedule.getLecture()),
            schedule.getDate().toString(),
            schedule.getCount()
        );
    }
}
