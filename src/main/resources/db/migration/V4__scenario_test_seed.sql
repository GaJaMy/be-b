-- 목적:
-- 1) 무료 강의 / 무료 환불 시나리오 검증
-- 2) 동일 판매에 대한 다중 취소 누적 검증
-- 3) 월 경계(KST) 판매/취소 시나리오 검증
-- 4) 서로 다른 수수료율 판매 혼합 정산 검증
-- 5) Settlement 상태(PENDING / CONFIRMED / PAID) 조회 검증
-- 6) 과거 판매 수수료율이 이후 월 취소 정산에도 유지되며, 같은 월 신규 판매와 함께 계산되는지 검증
-- 7) 환불만 있는 월에 수수료가 0원으로 처리되고 정산 예정 금액이 음수가 되는지 검증
-- 8) 플랫폼 수수료는 양수지만 정산 예정 금액은 음수가 되는지 검증
-- 9) 판매만 존재하는 순수 정산 검증
-- 10) 순 판매 금액이 정확히 0원이 되는 정산 검증
-- 11) 수수료 계산 시 소수점 올림 검증
-- 12) 여러 수수료율 판매와 환불이 함께 있는 복합 정산 검증

INSERT INTO creator (id, login_id, password_hash, name, created_at, updated_at)
VALUES ('creator-scenario-1', 'creator_scenario_1', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사1', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-2', 'creator_scenario_2', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사2', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-3', 'creator_scenario_3', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사3', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-4', 'creator_scenario_4', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사4', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-5', 'creator_scenario_5', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사5', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-6', 'creator_scenario_6', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사6', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-7', 'creator_scenario_7', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사7', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-8', 'creator_scenario_8', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사8', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-9', 'creator_scenario_9', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사9', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-10', 'creator_scenario_10', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사10', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-11', 'creator_scenario_11', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사11', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('creator-scenario-12', 'creator_scenario_12', '$2y$10$iEGXcQdYBIeABw0f81dG2.oamhoLLzeRd3l3OpHCrHMvJdxdCcA22', '시나리오강사12', '2025-04-01 00:00:00', '2025-04-01 00:00:00');

INSERT INTO course (id, creator_id, title, created_at, updated_at)
VALUES ('course-scenario-1', 'creator-scenario-1', '무료 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-2', 'creator-scenario-2', '다중 취소 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-3', 'creator-scenario-3', '월 경계 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-4', 'creator-scenario-4', '혼합 수수료 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-5', 'creator-scenario-6', '과거 수수료율 취소 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-6', 'creator-scenario-7', '환불만 있는 월 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-7', 'creator-scenario-8', '수수료 양수 정산 음수 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-8', 'creator-scenario-9', '순수 판매 정산 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-9', 'creator-scenario-10', '순매출 0 정산 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-10', 'creator-scenario-11', '소수점 올림 정산 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('course-scenario-11', 'creator-scenario-12', '복합 혼합 정산 강의', '2025-04-01 00:00:00', '2025-04-01 00:00:00');

-- 시나리오 1: 무료 강의 / 무료 환불
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-1', 'course-scenario-1', 'creator-scenario-1', 'student-scenario-free-1', 0, 20, '2025-04-05 10:00:00', '2025-04-05 10:00:00', '2025-04-05 10:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-1', 'sale-scenario-1', 'creator-scenario-1', 0, '2025-04-06 10:00:00', '2025-04-06 10:00:00', '2025-04-06 10:00:00');

-- 시나리오 2: 동일 판매에 대한 다중 취소 누적 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-2', 'course-scenario-2', 'creator-scenario-2', 'student-scenario-multi-1', 100000, 20, '2025-04-10 09:00:00', '2025-04-10 09:00:00', '2025-04-10 09:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-2', 'sale-scenario-2', 'creator-scenario-2', 30000, '2025-04-11 10:00:00', '2025-04-11 10:00:00', '2025-04-11 10:00:00'),
       ('cancel-scenario-3', 'sale-scenario-2', 'creator-scenario-2', 20000, '2025-04-12 11:00:00', '2025-04-12 11:00:00', '2025-04-12 11:00:00');

-- 시나리오 3: 월 경계(KST) 판매/취소 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-3', 'course-scenario-3', 'creator-scenario-3', 'student-scenario-boundary-1', 70000, 25, '2025-04-30 23:59:00', '2025-04-30 23:59:00', '2025-04-30 23:59:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-4', 'sale-scenario-3', 'creator-scenario-3', 70000, '2025-05-01 00:01:00', '2025-05-01 00:01:00', '2025-05-01 00:01:00');

-- 시나리오 4: 서로 다른 수수료율 판매 혼합 정산 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-4', 'course-scenario-4', 'creator-scenario-4', 'student-scenario-rate-1', 50000, 25, '2025-05-03 12:00:00', '2025-05-03 12:00:00', '2025-05-03 12:00:00'),
       ('sale-scenario-5', 'course-scenario-4', 'creator-scenario-4', 'student-scenario-rate-2', 50000, 30, '2025-05-20 12:00:00', '2025-05-20 12:00:00', '2025-05-20 12:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-5', 'sale-scenario-5', 'creator-scenario-4', 10000, '2025-05-21 09:00:00', '2025-05-21 09:00:00', '2025-05-21 09:00:00');

-- 시나리오 5: Settlement 상태 조회 검증
INSERT INTO settlement (
    id,
    creator_id,
    settlement_year_month,
    gross_sales_amount,
    refund_amount,
    net_sales_amount,
    platform_fee_amount,
    expected_payout_amount,
    sale_count,
    cancel_count,
    status,
    requested_at,
    confirmed_at,
    paid_at,
    created_at,
    updated_at
)
VALUES ('settlement-scenario-1', 'creator-scenario-5', '2025-01', 100000, 0, 100000, 20000, 80000, 2, 0, 'PENDING', '2025-02-01 09:00:00', NULL, NULL, '2025-02-01 09:00:00', '2025-02-01 09:00:00'),
       ('settlement-scenario-2', 'creator-scenario-5', '2025-02', 150000, 10000, 140000, 28000, 112000, 2, 1, 'CONFIRMED', '2025-03-01 09:00:00', '2025-03-02 10:00:00', NULL, '2025-03-01 09:00:00', '2025-03-02 10:00:00'),
       ('settlement-scenario-3', 'creator-scenario-5', '2025-03', 120000, 0, 120000, 24000, 96000, 1, 0, 'PAID', '2025-04-01 09:00:00', '2025-04-02 10:00:00', '2025-04-03 11:00:00', '2025-04-01 09:00:00', '2025-04-03 11:00:00');

-- 시나리오 6: 4월 판매 수수료율이 5월/6월 취소 정산에도 유지되며, 5월 신규 판매와 함께 계산되는지 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-6', 'course-scenario-5', 'creator-scenario-6', 'student-scenario-legacy-1', 50000, 20, '2025-04-10 13:00:00', '2025-04-10 13:00:00', '2025-04-10 13:00:00'),
       ('sale-scenario-7', 'course-scenario-5', 'creator-scenario-6', 'student-scenario-legacy-2', 80000, 25, '2025-04-20 15:00:00', '2025-04-20 15:00:00', '2025-04-20 15:00:00'),
       ('sale-scenario-8', 'course-scenario-5', 'creator-scenario-6', 'student-scenario-legacy-3', 40000, 30, '2025-05-15 14:00:00', '2025-05-15 14:00:00', '2025-05-15 14:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-6', 'sale-scenario-6', 'creator-scenario-6', 10000, '2025-05-10 10:00:00', '2025-05-10 10:00:00', '2025-05-10 10:00:00'),
       ('cancel-scenario-7', 'sale-scenario-7', 'creator-scenario-6', 20000, '2025-06-10 10:00:00', '2025-06-10 10:00:00', '2025-06-10 10:00:00');

-- 시나리오 7: 환불만 있는 월에 수수료가 0원으로 처리되고 정산 예정 금액이 음수가 되는지 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-9', 'course-scenario-6', 'creator-scenario-7', 'student-scenario-negative-1', 30000, 20, '2025-04-08 11:00:00', '2025-04-08 11:00:00', '2025-04-08 11:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-8', 'sale-scenario-9', 'creator-scenario-7', 10000, '2025-05-12 16:00:00', '2025-05-12 16:00:00', '2025-05-12 16:00:00');

-- 시나리오 8: 플랫폼 수수료는 양수지만 정산 예정 금액은 음수가 되는지 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-10', 'course-scenario-7', 'creator-scenario-8', 'student-scenario-mixed-1', 30000, 20, '2025-04-05 09:00:00', '2025-04-05 09:00:00', '2025-04-05 09:00:00'),
       ('sale-scenario-11', 'course-scenario-7', 'creator-scenario-8', 'student-scenario-mixed-2', 10000, 30, '2025-05-15 13:00:00', '2025-05-15 13:00:00', '2025-05-15 13:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-9', 'sale-scenario-10', 'creator-scenario-8', 30000, '2025-05-20 10:00:00', '2025-05-20 10:00:00', '2025-05-20 10:00:00');

-- 시나리오 9: 판매만 존재하는 순수 정산 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-12', 'course-scenario-8', 'creator-scenario-9', 'student-scenario-sales-only-1', 20000, 35, '2025-06-05 10:00:00', '2025-06-05 10:00:00', '2025-06-05 10:00:00'),
       ('sale-scenario-13', 'course-scenario-8', 'creator-scenario-9', 'student-scenario-sales-only-2', 30000, 35, '2025-06-18 11:00:00', '2025-06-18 11:00:00', '2025-06-18 11:00:00');

-- 시나리오 10: 순 판매 금액이 정확히 0원이 되는 정산 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-14', 'course-scenario-9', 'creator-scenario-10', 'student-scenario-net-zero-1', 10000, 35, '2025-06-08 10:30:00', '2025-06-08 10:30:00', '2025-06-08 10:30:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-10', 'sale-scenario-14', 'creator-scenario-10', 10000, '2025-06-09 09:00:00', '2025-06-09 09:00:00', '2025-06-09 09:00:00');

-- 시나리오 11: 수수료 계산 시 소수점 올림 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-15', 'course-scenario-10', 'creator-scenario-11', 'student-scenario-rounding-1', 10001, 35, '2025-06-12 14:00:00', '2025-06-12 14:00:00', '2025-06-12 14:00:00');

-- 시나리오 12: 여러 수수료율 판매와 환불이 함께 있는 복합 정산 검증
INSERT INTO sale_record (id, course_id, creator_id, student_id, amount, fee_rate_percent, paid_at, created_at, updated_at)
VALUES ('sale-scenario-16', 'course-scenario-11', 'creator-scenario-12', 'student-scenario-complex-1', 30000, 35, '2025-06-05 09:00:00', '2025-06-05 09:00:00', '2025-06-05 09:00:00'),
       ('sale-scenario-17', 'course-scenario-11', 'creator-scenario-12', 'student-scenario-complex-2', 20000, 35, '2025-06-18 15:00:00', '2025-06-18 15:00:00', '2025-06-18 15:00:00'),
       ('sale-scenario-18', 'course-scenario-11', 'creator-scenario-12', 'student-scenario-complex-3', 40000, 40, '2025-07-12 11:00:00', '2025-07-12 11:00:00', '2025-07-12 11:00:00'),
       ('sale-scenario-19', 'course-scenario-11', 'creator-scenario-12', 'student-scenario-complex-4', 30000, 45, '2025-07-25 16:00:00', '2025-07-25 16:00:00', '2025-07-25 16:00:00');

INSERT INTO cancellation_record (id, sale_record_id, creator_id, refund_amount, canceled_at, created_at, updated_at)
VALUES ('cancel-scenario-11', 'sale-scenario-16', 'creator-scenario-12', 10000, '2025-07-13 10:00:00', '2025-07-13 10:00:00', '2025-07-13 10:00:00'),
       ('cancel-scenario-12', 'sale-scenario-18', 'creator-scenario-12', 5000, '2025-07-28 09:00:00', '2025-07-28 09:00:00', '2025-07-28 09:00:00');

-- 수수료율 이력 조회 검증용 데이터
INSERT INTO fee_policy_history (id, fee_rate_percent, effective_started_at, created_at, updated_at)
VALUES ('fee-policy-history-scenario-1', 20, '2025-04-01 00:00:00', '2025-04-01 00:00:00', '2025-04-01 00:00:00'),
       ('fee-policy-history-scenario-2', 25, '2025-04-15 00:00:00', '2025-04-15 00:00:00', '2025-04-15 00:00:00'),
       ('fee-policy-history-scenario-3', 30, '2025-05-10 00:00:00', '2025-05-10 00:00:00', '2025-05-10 00:00:00'),
       ('fee-policy-history-scenario-4', 35, '2025-06-01 00:00:00', '2025-06-01 00:00:00', '2025-06-01 00:00:00'),
       ('fee-policy-history-scenario-5', 40, '2025-07-10 00:00:00', '2025-07-10 00:00:00', '2025-07-10 00:00:00'),
       ('fee-policy-history-scenario-6', 45, '2025-07-20 00:00:00', '2025-07-20 00:00:00', '2025-07-20 00:00:00');
