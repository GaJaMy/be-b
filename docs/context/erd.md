# ERD

## 문서 기준
이 문서는 현재 설계 기준의 저장 대상 테이블을 정리한다.  
판매/취소 원천 데이터와 별도로 실제 정산 상태를 관리하기 위한 `settlement` 테이블을 포함한다.

## 1. `creator`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 크리에이터 ID |
| `login_id` | `varchar(100)` | N | N | N | 로그인 ID, 유니크 |
| `password_hash` | `varchar(255)` | N | N | N | 암호화된 비밀번호 |
| `name` | `varchar(100)` | N | N | N | 크리에이터 이름 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 2. `admin`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 운영자 ID |
| `login_id` | `varchar(100)` | N | N | N | 로그인 ID, 유니크 |
| `password_hash` | `varchar(255)` | N | N | N | 암호화된 비밀번호 |
| `name` | `varchar(100)` | N | N | N | 운영자 이름 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 3. `course`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 강의 ID |
| `creator_id` | `varchar(50)` | N | Y | N | 소유 크리에이터 ID |
| `title` | `varchar(200)` | N | N | N | 강의명 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 4. `sale_record`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 판매 ID |
| `course_id` | `varchar(50)` | N | Y | N | 판매된 강의 ID |
| `creator_id` | `varchar(50)` | N | Y | N | 정산 대상 크리에이터 ID |
| `student_id` | `varchar(50)` | N | N | N | 수강생 ID |
| `amount` | `bigint` | N | N | N | 결제 금액 |
| `fee_rate_percent` | `int` | N | N | N | 판매 당시 적용된 플랫폼 수수료율 퍼센트, 예: `20` |
| `paid_at` | `datetime` | N | N | N | 결제 완료 시각 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 5. `cancellation_record`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 취소 ID |
| `sale_record_id` | `varchar(50)` | N | Y | N | 원본 판매 ID |
| `creator_id` | `varchar(50)` | N | Y | N | 정산 대상 크리에이터 ID |
| `refund_amount` | `bigint` | N | N | N | 환불 금액 |
| `canceled_at` | `datetime` | N | N | N | 취소 시각 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 6. `settlement`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 정산 ID |
| `creator_id` | `varchar(50)` | N | Y | N | 정산 대상 크리에이터 ID |
| `settlement_year_month` | `varchar(7)` | N | N | N | 정산 대상 연월, 예: `2025-03` |
| `gross_sales_amount` | `bigint` | N | N | N | 총 판매 금액 |
| `refund_amount` | `bigint` | N | N | N | 총 환불 금액 |
| `net_sales_amount` | `bigint` | N | N | N | 순 판매 금액 |
| `platform_fee_amount` | `bigint` | N | N | N | 플랫폼 수수료 |
| `expected_payout_amount` | `bigint` | N | N | N | 정산 예정 금액 |
| `sale_count` | `int` | N | N | N | 판매 건수 |
| `cancel_count` | `int` | N | N | N | 취소 건수 |
| `status` | `varchar(20)` | N | N | N | 정산 상태, `PENDING` / `CONFIRMED` / `PAID` |
| `requested_at` | `datetime` | N | N | N | 정산 생성 시각 |
| `confirmed_at` | `datetime` | N | N | Y | 정산 확정 시각 |
| `paid_at` | `datetime` | N | N | Y | 지급 완료 시각 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 7. `fee_policy`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 수수료율 정책 ID |
| `fee_rate_percent` | `int` | N | N | N | 현재 적용 수수료율 퍼센트, 예: `20` |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 8. `fee_policy_history`

| 컬럼명 | 타입 | PK | FK | NULL | 설명 |
| --- | --- | --- | --- | --- | --- |
| `id` | `varchar(50)` | Y | N | N | 수수료율 이력 ID |
| `fee_rate_percent` | `int` | N | N | N | 변경 이력으로 기록된 수수료율 퍼센트, 예: `20` |
| `effective_started_at` | `datetime` | N | N | N | 변경된 수수료율이 실제로 적용되기 시작한 시각 |
| `created_at` | `datetime` | N | N | N | 생성 일시 |
| `updated_at` | `datetime` | N | N | N | 수정 일시 |

## 관계 정리

| 부모 | 자식 | 관계 | 설명 |
| --- | --- | --- | --- |
| `creator` | `course` | 1:N | 한 크리에이터는 여러 강의를 가질 수 있다 |
| `creator` | `sale_record` | 1:N | 한 크리에이터는 여러 판매 내역을 가진다 |
| `creator` | `cancellation_record` | 1:N | 한 크리에이터는 여러 취소 내역을 가진다 |
| `creator` | `settlement` | 1:N | 한 크리에이터는 여러 월별 정산을 가진다 |
| `course` | `sale_record` | 1:N | 한 강의는 여러 판매 내역을 가진다 |
| `sale_record` | `cancellation_record` | 1:N | 한 판매는 여러 취소 내역을 가질 수 있다 |

## 제약조건 및 규칙

| 항목 | 내용 |
| --- | --- |
| 로그인 ID | `creator.login_id`, `admin.login_id`는 각각 유니크 |
| 금액 | `amount`, `refund_amount`는 0 이상이어야 하며, `settlement`의 순매출과 정산 예정 금액은 환불 차감으로 음수가 될 수 있다. 다만 `platform_fee_amount`는 최소 0원으로 처리한다 |
| 부분 환불 | `refund_amount`는 원 결제 금액 이하로 제한한다 |
| 시간 기준 | 저장은 KST 기준 `LocalDateTime`으로 일관되게 처리하고, API 입력 시간은 KST로 정규화한 뒤 저장한다 |
| 정산 기준 | 판매는 `paid_at`, 취소는 `canceled_at`을 기준으로 집계한다 |
| 정산 유니크 | `settlement`는 `creator_id + settlement_year_month` 조합이 유니크여야 한다 |
| 정산 상태 | `status`는 `PENDING -> CONFIRMED -> PAID` 순서만 허용한다 |
| 현재 수수료율 | `fee_policy`는 현재 적용할 수수료율 정책을 저장하며, 애플리케이션에서 단일 row로 관리한다 |
| 판매 수수료율 스냅샷 | 판매 등록 시점의 `fee_policy.fee_rate_percent`를 `sale_record.fee_rate_percent`에 저장한다 |
| 수수료율 적용 | 판매분은 해당 `sale_record.fee_rate_percent`를 적용하고, 환불분은 원본 `sale_record.fee_rate_percent`를 적용한다 |
| 수수료율 변경 시점 | 운영자가 현재 수수료율을 변경하면 변경 시점부터 즉시 적용되며, 이후 등록되는 판매부터 새 수수료율을 사용한다 |
| 수수료율 이력 | `fee_policy_history`는 현재 수수료율 변경 이력을 조회하기 위한 기록이며, 정산 계산의 직접 FK 기준으로 사용하지 않는다 |

## 비고
- `sale_record.creator_id`와 `cancellation_record.creator_id`는 정산 조회 성능과 단순한 집계를 위해 중복 보관한다.
- 운영자 정산 집계는 `admin` 테이블과 직접 관계를 맺지 않는다. 운영자는 조회 주체일 뿐, 정산 데이터의 소유자는 아니다.
- `settlement`는 판매/취소 원천 데이터를 복사 저장한 결과가 아니라, 특정 월 정산을 관리하기 위한 스냅샷 성격의 데이터로 본다.
- 크리에이터 월별 정산 조회 API와 `settlement` 조회 API는 분리한다. 전자는 예상 정산 조회이고, 후자는 생성된 정산 관리 데이터 조회이다.
- 중복 정산 방지는 `creator_id + settlement_year_month` 유니크 제약과 애플리케이션 레벨 사전 조회를 함께 두는 편이 안전하다.
- 현재 수수료율은 단일 row의 `fee_policy`에서 관리하고, 변경 이력은 `fee_policy_history`에 보관한다.
- 판매 당시 적용된 수수료율은 `sale_record.fee_rate_percent`에 스냅샷으로 보관한다.
- 과거 정산은 이후 수수료율이 바뀌어도 이미 저장된 판매별 수수료율과 정산 금액 스냅샷을 기준으로 유지된다.
- `fee_policy_history`는 수수료율 변경 이력을 남기기 위한 테이블이며, `settlement`와 직접 관계를 맺지 않는다.
- 플랫폼 수수료는 음수가 될 수 없으므로 최소 0원으로 처리한다.
- `expected_payout_amount`가 음수인 경우, 이는 해당 정산에서 지급이 아니라 차감으로 처리되어야 할 금액이 발생했음을 의미한다.
