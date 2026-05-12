# 도메인 설계 초안

## 설계 목표
이 프로젝트의 핵심은 판매, 취소, 정산 계산, 인증을 서로 혼합하지 않고 분리하는 것이다.  
정산은 별도 테이블에 누적 저장하는 방식보다, 우선 판매/취소 원천 데이터(`SaleRecord`, `CancellationRecord`)를 기준으로 조회 시 계산하는 구조가 과제 범위에 더 적합하다.

## 주요 애그리거트
### 1. Creator
- 크리에이터 계정
- 필드 예시: `id`, `loginId`, `passwordHash`, `name`
- 책임: 자신의 강의와 정산 조회 주체

### 2. Admin
- 운영자 계정
- 필드 예시: `id`, `loginId`, `passwordHash`, `name`
- 책임: 전체 정산 현황 조회 주체

### 3. Course
- 크리에이터가 소유하는 강의
- 필드 예시: `id`, `creatorId`, `title`
- 관계: `Creator` 1 : N `Course`

### 4. SaleRecord
- 결제 완료된 판매 원천 데이터
- 필드 예시: `id`, `courseId`, `creatorId`, `studentId`, `amount`, `paidAt`
- 관계: `Course` 1 : N `SaleRecord`
- 비고: `creatorId`는 조회 성능과 정산 집계를 위해 중복 보관하는 편이 실용적이다.

### 5. CancellationRecord
- 판매 취소 또는 환불 데이터
- 필드 예시: `id`, `saleRecordId`, `creatorId`, `refundAmount`, `canceledAt`
- 관계: `SaleRecord` 1 : N `CancellationRecord`
- 비고: 부분 환불을 허용하므로 환불 금액은 원 결제 금액 이하의 값으로 검증해야 한다.

## 정산 도메인 설계
### SettlementPolicy
- 규칙 고정값을 한 곳에 모으는 정책 객체
- 예시:
  - 플랫폼 수수료율 `20%`
  - 기준 시간대 `Asia/Seoul`
  - 월 경계 계산 규칙

### SettlementService
- 책임:
  - 월별 크리에이터 정산 계산
  - 기간별 운영자 정산 집계
  - 판매 기준일과 취소 기준일을 분리한 계산
- 비고:
  - 정산 결과는 DB에 저장하는 별도 엔티티보다 조회 시 계산하는 방식으로 처리한다.
  - API 응답 DTO는 애플리케이션 계층에서 별도로 정의한다.

## 인증/인가 설계
- 로그인 주체는 `Creator`, `Admin` 두 종류로 분리한다.
- Spring Security에서는 `ROLE_CREATOR`, `ROLE_ADMIN` 방식이 단순하다.
- JWT 기반 인증을 사용하면 현재 프로젝트 의존성과도 잘 맞는다.
- 권한 경계 예시:
  - 크리에이터: 자신의 판매 내역, 자신의 월별 정산 조회
  - 운영자: 전체 크리에이터 정산 집계 조회

## 샘플 데이터 기반 검증 포인트
- `creator-1`, `2025-03`
  - 총 판매 `260000`
  - 환불 `110000`
  - 순 판매 `150000`
  - 수수료 `30000`
  - 정산 예정 `120000`
- 부분 환불
  - `sale-4` + `cancel-2`는 `30000`만 차감
- 월 경계 처리
  - `sale-5`는 2025-01 판매
  - 연결 취소는 2025-02 환불로 별도 반영
- 빈 월 조회
  - `creator-3`, `2025-03`는 0원 기반 응답을 권장

## 추천 패키지 구조
- `domain/creator`
- `domain/admin`
- `domain/course`
- `domain/sale`
- `domain/settlement`
- `security`
- `common`

## 구현 원칙
- 금액은 `BigDecimal`보다 정수 원화 단위(`Long`)를 우선 검토한다.
- 시간은 `OffsetDateTime` 또는 `ZonedDateTime`으로 받고, 내부 정산 계산은 KST 기준으로 변환한다.
- 정산 로직은 컨트롤러나 엔티티에 넣지 않고 `SettlementService`에 집중시킨다.
- 테스트는 반드시 샘플 데이터 시나리오를 그대로 재현하는 방향으로 작성한다.
