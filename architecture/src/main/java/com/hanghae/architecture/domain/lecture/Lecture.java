package com.hanghae.architecture.domain.lecture;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
