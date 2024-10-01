package com.hanghae.architecture.domain.lecture;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tutor;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Subject subject;

    @ColumnDefault("30")
    private int capacity;

}
