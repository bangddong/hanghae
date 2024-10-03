package com.hanghae.architecture.infrastructure.repository;

import com.hanghae.architecture.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaLectureRepository extends JpaRepository<Lecture, Long> {
    Optional<Lecture> findById(long lectureId);
}
