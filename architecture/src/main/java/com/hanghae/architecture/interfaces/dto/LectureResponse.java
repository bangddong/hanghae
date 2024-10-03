package com.hanghae.architecture.interfaces.dto;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;

public record LectureResponse(
        String tutor,
        Subject subject
) {
    public static LectureResponse fromEntity(Lecture lecture) {
        return new LectureResponse(
                lecture.getTutor(),
                lecture.getSubject()
        );
    }
}
