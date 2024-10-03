-- Lecture 테이블에 기초 데이터 삽입
INSERT INTO lecture (id, tutor, subject) VALUES (1, '이석범', 'JAVA');
INSERT INTO lecture (id, tutor, subject) VALUES (2, '렌', 'SPRING');
INSERT INTO lecture (id, tutor, subject) VALUES (3, '허재', 'REACT');

-- Schedule 테이블에 기초 데이터 삽입
INSERT INTO schedule (id, lecture_id, capacity, date, count) VALUES (1, 1, 30, '2024-10-01', 29);
INSERT INTO schedule (id, lecture_id, capacity, date, count) VALUES (2, 2, 30, '2024-10-05', 30);
INSERT INTO schedule (id, lecture_id, capacity, date, count) VALUES (3, 3, 30, '2024-10-10', 29);
