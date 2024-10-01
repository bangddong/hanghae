package com.hanghae.architecture.domain.lecture;

import java.util.Optional;

public interface LectureRepository {
    Optional<Lecture> findById(long lectureId);
}
