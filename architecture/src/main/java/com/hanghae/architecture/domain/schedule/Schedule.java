package com.hanghae.architecture.domain.schedule;

import com.hanghae.architecture.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private final int capacity = 30;

    @Column(nullable = false)
    private int count;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public boolean canReserve() {
        return count < capacity;
    }

    public void reserve() {
        if (canReserve()) {
            count++;
        } else {
            throw new IllegalStateException("수강 인원이 초과되었습니다.");
        }
    }

    protected Schedule(Lecture lecture, LocalDate date, int count) {
        this.lecture = lecture;
        this.date = date;
        this.count = count;
    }

    public static Schedule of(Lecture lecture, LocalDate date, int count) {
        return new Schedule(
                lecture,
                date,
                count
        );
    }
}
