package com.hanghae.architecture.infrastructure;

import com.hanghae.architecture.domain.lecture.Lecture;
import com.hanghae.architecture.domain.lecture.Subject;
import com.hanghae.architecture.infrastructure.repository.JpaLectureRepository;
import com.hanghae.architecture.infrastructure.repository.LectureRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LectureRepositoryJpaAdapterTest {

    @Mock
    private JpaLectureRepository jpaLectureRepository;

    @InjectMocks
    private LectureRepositoryJpaAdapter lectureRepositoryJpaAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindById() {
        // Given
        Lecture lecture = Lecture.of("이석범", Subject.JAVA);
        when(jpaLectureRepository.findById(1L)).thenReturn(Optional.of(lecture));

        // When
        Optional<Lecture> result = lectureRepositoryJpaAdapter.findById(1L);

        // Then
        assertEquals(Subject.JAVA, result.get().getSubject());
        verify(jpaLectureRepository, times(1)).findById(1L);
    }

}