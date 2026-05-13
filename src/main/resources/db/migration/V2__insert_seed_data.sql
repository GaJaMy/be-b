INSERT INTO course (id, creator_id, title, created_at, updated_at)
VALUES ('course-1', 'creator-1', 'Spring Boot 입문', '2025-03-01 00:00:00', '2025-03-01 00:00:00'),
       ('course-2', 'creator-1', 'JPA 실전', '2025-03-01 00:00:00', '2025-03-01 00:00:00'),
       ('course-3', 'creator-2', 'Kotlin 기초', '2025-03-01 00:00:00', '2025-03-01 00:00:00'),
       ('course-4', 'creator-3', 'MSA 설계', '2025-03-01 00:00:00', '2025-03-01 00:00:00');

INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, paid_at, created_at, updated_at)
VALUES ('sale-1', 'course-1', 'creator-1', 'student-1', 50000, '2025-03-05 10:00:00', '2025-03-05 10:00:00', '2025-03-05 10:00:00'),
       ('sale-2', 'course-1', 'creator-1', 'student-2', 50000, '2025-03-15 14:30:00', '2025-03-15 14:30:00', '2025-03-15 14:30:00'),
       ('sale-3', 'course-2', 'creator-1', 'student-3', 80000, '2025-03-20 09:00:00', '2025-03-20 09:00:00', '2025-03-20 09:00:00'),
       ('sale-4', 'course-2', 'creator-1', 'student-4', 80000, '2025-03-22 11:00:00', '2025-03-22 11:00:00', '2025-03-22 11:00:00'),
       ('sale-5', 'course-3', 'creator-2', 'student-5', 60000, '2025-01-31 23:30:00', '2025-01-31 23:30:00', '2025-01-31 23:30:00'),
       ('sale-6', 'course-3', 'creator-2', 'student-6', 60000, '2025-03-10 16:00:00', '2025-03-10 16:00:00', '2025-03-10 16:00:00'),
       ('sale-7', 'course-4', 'creator-3', 'student-7', 120000, '2025-02-14 10:00:00', '2025-02-14 10:00:00', '2025-02-14 10:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-1', 'sale-3', 'creator-1', 80000, '2025-03-20 10:00:00', '2025-03-20 10:00:00', '2025-03-20 10:00:00'),
       ('cancel-2', 'sale-4', 'creator-1', 30000, '2025-03-22 12:00:00', '2025-03-22 12:00:00', '2025-03-22 12:00:00'),
       ('cancel-3', 'sale-5', 'creator-2', 60000, '2025-02-01 00:00:00', '2025-02-01 00:00:00', '2025-02-01 00:00:00');
