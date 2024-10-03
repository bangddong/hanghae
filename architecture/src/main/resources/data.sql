-- Lecture 테이블에 기초 데이터 삽입
INSERT INTO lecture (tutor, subject) VALUES ('이석범', 'JAVA');
INSERT INTO lecture (tutor, subject) VALUES ('렌', 'SPRING');
INSERT INTO lecture (tutor, subject) VALUES ('허재', 'REACT');

-- Schedule 테이블에 기초 데이터 삽입
INSERT INTO schedule (lecture_id, capacity, date, count) VALUES (1, 30, '2024-10-01', 0);
INSERT INTO schedule (lecture_id, capacity, date, count) VALUES ( 2, 30, '2024-10-05', 0);
INSERT INTO schedule (lecture_id, capacity, date, count) VALUES ( 3, 30, '2024-10-10', 0);
