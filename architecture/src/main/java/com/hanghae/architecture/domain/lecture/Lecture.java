package com.hanghae.architecture.domain.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tutor;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    protected Lecture(String tutor, Subject subject) {
        this.tutor = tutor;
        this.subject = subject;
    }

    public static Lecture of(String tutor, Subject subject) {
        return new Lecture(
                tutor,
                subject
        );
    }

}
