package com.hanghae.architecture.infrastructure.repository;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class LectureRepositoryJpaAdapter implements LectureRepository {
    private final JpaLectureRepository jpaLectureRepository;

    @Override
    public Optional<Lecture> findById(long lectureId) {
        return jpaLectureRepository.findById(lectureId);
    }
}
