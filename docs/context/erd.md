# ERD

## 문서 기준
이 문서는 현재 설계 기준의 저장 대상 테이블만 정리한다.  
`Settlement`은 별도 테이블로 저장하지 않고 `SaleRecord`, `CancellationRecord`를 기준으로 계산하므로 ERD에서 제외한다.

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

## 관계 정리

| 부모 | 자식 | 관계 | 설명 |
| --- | --- | --- | --- |
| `creator` | `course` | 1:N | 한 크리에이터는 여러 강의를 가질 수 있다 |
| `creator` | `sale_record` | 1:N | 한 크리에이터는 여러 판매 내역을 가진다 |
| `creator` | `cancellation_record` | 1:N | 한 크리에이터는 여러 취소 내역을 가진다 |
| `course` | `sale_record` | 1:N | 한 강의는 여러 판매 내역을 가진다 |
| `sale_record` | `cancellation_record` | 1:N | 한 판매는 여러 취소 내역을 가질 수 있다 |

## 제약조건 및 규칙

| 항목 | 내용 |
| --- | --- |
| 로그인 ID | `creator.login_id`, `admin.login_id`는 각각 유니크 |
| 금액 | `amount`, `refund_amount`는 0보다 커야 한다 |
| 부분 환불 | `refund_amount`는 원 결제 금액 이하로 제한한다 |
| 시간 기준 | 저장은 UTC 또는 KST 중 하나로 일관되게 하고, 정산 계산은 KST 기준으로 처리한다 |
| 정산 기준 | 판매는 `paid_at`, 취소는 `canceled_at`을 기준으로 집계한다 |

## 비고
- `sale_record.creator_id`와 `cancellation_record.creator_id`는 정산 조회 성능과 단순한 집계를 위해 중복 보관한다.
- 운영자 정산 집계는 `admin` 테이블과 직접 관계를 맺지 않는다. 운영자는 조회 주체일 뿐, 정산 데이터의 소유자는 아니다.
