# 도메인 설계 초안

## 문서 목적
이 문서는 이 프로젝트에서 어떤 도메인 엔티티를 두고, 각 엔티티가 어떤 상태와 관계를 가지는지 정리하기 위한 문서이다.  
구현 계층의 `UseCase`, `Service`, DTO 구조보다 저장 대상과 도메인 규칙을 명확히 하는 데 초점을 둔다.

## 핵심 엔티티
### 1. Creator
- 크리에이터 계정
- 필드 예시: `id`, `loginId`, `passwordHash`, `name`, `createdAt`, `updatedAt`
- 책임:
  - 자신의 강의 소유 주체
  - 자신의 판매 내역 및 정산 조회 주체
  - 자신의 정산 생성 요청 주체

### 2. Admin
- 운영자 계정
- 필드 예시: `id`, `loginId`, `passwordHash`, `name`, `createdAt`, `updatedAt`
- 책임:
  - 생성된 정산 조회 주체
  - 정산 확정 주체
  - 정산 지급 처리 주체

### 3. Course
- 크리에이터가 소유하는 강의
- 필드 예시: `id`, `creatorId`, `title`, `createdAt`, `updatedAt`
- 관계:
  - `Creator` 1 : N `Course`

### 4. SaleRecord
- 결제 완료된 판매 원천 데이터
- 필드 예시: `id`, `courseId`, `creatorId`, `studentId`, `amount`, `feeRatePercent`, `paidAt`, `createdAt`, `updatedAt`
- 관계:
  - `Course` 1 : N `SaleRecord`
  - `Creator` 1 : N `SaleRecord`
- 비고:
  - `creatorId`는 조회 성능과 정산 집계를 위해 중복 보관하는 편이 실용적이다.
  - `feeRatePercent`는 판매 등록 시점의 현재 수수료율을 스냅샷으로 저장한다.
  - 이후 현재 수수료율이나 수수료율 이력이 변경되어도 이미 등록된 판매의 수수료율은 바뀌지 않는다.
  - `paidAt`은 API 입력이 `OffsetDateTime`이어도 내부 저장은 KST 기준 `LocalDateTime`으로 정규화한다.

### 5. CancellationRecord
- 판매 취소 또는 환불 원천 데이터
- 필드 예시: `id`, `saleRecordId`, `creatorId`, `refundAmount`, `canceledAt`, `createdAt`, `updatedAt`
- 관계:
  - `SaleRecord` 1 : N `CancellationRecord`
  - `Creator` 1 : N `CancellationRecord`
- 비고:
  - 부분 환불을 허용하므로 환불 금액은 원 결제 금액 이하로 검증해야 한다.
  - `canceledAt`도 내부 저장은 KST 기준 `LocalDateTime`으로 정규화한다.

### 6. Settlement
- 크리에이터가 특정 월에 대해 실제로 생성한 정산 데이터
- 필드 예시:
  - `id`
  - `creatorId`
  - `yearMonth`
  - `grossSalesAmount`
  - `refundAmount`
  - `netSalesAmount`
  - `platformFeeAmount`
  - `expectedPayoutAmount`
  - `saleCount`
  - `cancelCount`
  - `status`
  - `requestedAt`
  - `confirmedAt`
  - `paidAt`
  - `createdAt`
  - `updatedAt`
- 관계:
  - `Creator` 1 : N `Settlement`
- 비고:
  - `Settlement`는 크리에이터가 정산 생성 API를 호출했을 때 만들어지는 실제 관리 데이터이다.
  - 판매/취소 원천 데이터가 이후 바뀌더라도, 이미 생성된 `Settlement`는 당시 계산 결과를 저장한 스냅샷으로 본다.
  - 정산 계산의 수수료율 근거는 `FeePolicyHistory` FK가 아니라 원천 판매 내역의 `SaleRecord.feeRatePercent`이다.
  - 동일한 `creatorId + yearMonth` 조합에 대해서는 하나의 `Settlement`만 생성되도록 중복 정산을 막는다.

### 7. FeePolicy
- 현재 적용할 플랫폼 수수료율 정책을 관리하는 엔티티
- 필드 예시:
  - `id`
  - `feeRatePercent`
  - `createdAt`
  - `updatedAt`
- 비고:
  - 현재 수수료율만 저장하는 단일 row 엔티티로 관리한다.
  - 기본 플랫폼 수수료율 같은 현재 운영 설정은 이 엔티티에서 조회한다.

### 8. FeePolicyHistory
- 현재 수수료율 변경 이력을 관리하는 엔티티
- 필드 예시:
  - `id`
  - `feeRatePercent`
  - `targetYearMonth`
  - `createdAt`
  - `updatedAt`
- 비고:
  - 수수료율 이력은 운영자가 현재 수수료율을 언제 어떤 값으로 변경했는지 남기기 위한 기록이다.
  - 수수료율 이력은 기준 연월마다 하나만 존재하는 것으로 관리한다.
  - 정산 계산은 이력을 직접 참조하지 않고, 판매 당시 `SaleRecord`에 저장된 수수료율 스냅샷을 사용한다.

## 정산 상태
### SettlementStatus
- `PENDING`
  - 크리에이터가 정산을 생성한 상태
  - 운영자 확인 전
- `CONFIRMED`
  - 운영자가 정산 금액과 정산 대상을 확인하고 확정한 상태
- `PAID`
  - 운영자가 실제 지급까지 완료한 상태

### 상태 전이 규칙
- `PENDING -> CONFIRMED`
- `CONFIRMED -> PAID`
- 역방향 전이는 허용하지 않는 편이 단순하다.

## 도메인 관계 요약
- 한 `Creator`는 여러 `Course`를 가진다.
- 한 `Course`는 여러 `SaleRecord`를 가진다.
- 한 `SaleRecord`는 여러 `CancellationRecord`를 가질 수 있다.
- 한 `Creator`는 여러 `Settlement`를 가질 수 있다.
- `Settlement`는 `SaleRecord`, `CancellationRecord`를 직접 참조하기보다, 특정 월 기준 계산 결과를 금액 스냅샷으로 저장한다.

## 정산 규칙
- 판매 집계 기준은 `paidAt`이다.
- 취소 집계 기준은 `canceledAt`이다.
- 월 경계는 KST 기준으로 계산한다.
- 기본 플랫폼 수수료율은 `20%`이지만, 판매 등록 시점의 현재 수수료율을 `SaleRecord.feeRatePercent`에 저장한다.
- 판매분 수수료는 해당 판매의 `SaleRecord.feeRatePercent`를 기준으로 계산한다.
- 환불분 수수료 차감은 취소 시점의 현재 수수료율이 아니라 원본 판매의 `SaleRecord.feeRatePercent`를 기준으로 계산한다.
- 정산 예정 금액은 `순 판매 금액 - 플랫폼 수수료`이다.
- 판매 금액과 환불 금액은 무료 강의를 고려해 `0 이상`을 허용한다.
- 특정 월에 환불만 존재할 수 있으므로 정산의 순매출, 플랫폼 수수료, 정산 예정 금액은 음수가 될 수 있다.
- 동일한 크리에이터와 동일한 정산 대상 월에는 하나의 정산만 생성할 수 있다.
- 과거 정산은 이후 수수료율이 바뀌어도 재계산하지 않고, 판매별 수수료율 스냅샷과 정산 금액 스냅샷을 유지한다.
- 수수료율이 기간 중 변경될 수 있으므로 정산 수수료는 판매/환불의 원본 판매 수수료율을 기준으로 수수료율별 순액을 만든 뒤 합산한다.
- 같은 수수료율만 존재하는 경우 아래 과제 원문의 정산 공식과 동일한 결과가 된다.
  - 순 판매 금액 = 총 판매 금액 - 환불 금액
  - 플랫폼 수수료 = 순 판매 금액의 수수료율%

## 정산 조회와 정산 생성의 차이
- 크리에이터 월별 정산 조회 API
  - 저장된 `Settlement`를 조회하는 API가 아니다.
  - 해당 월에 지금 정산을 생성하면 얼마가 되는지 보여주는 예상 정산 조회 API이다.
- 정산 생성 API
  - 원천 판매/취소 데이터를 기준으로 금액을 계산한다.
  - 판매 금액은 각 `SaleRecord.feeRatePercent`를 기준으로 수수료를 계산한다.
  - 환불 금액은 원본 `SaleRecord.feeRatePercent`를 기준으로 수수료 차감분을 계산한다.
  - 계산 결과를 `Settlement(status = PENDING)`로 저장한다.
  - 이미 같은 `creatorId + yearMonth`의 `Settlement`가 있으면 생성하지 않는다.
- 운영자 정산 관리 API
  - 생성된 `Settlement`를 조회한다.
  - `PENDING` 정산을 `CONFIRMED`로 바꾼다.
  - `CONFIRMED` 정산을 `PAID`로 바꾼다.

## 인증 주체와 도메인 경계
- `Creator`
  - 자신의 판매 내역 조회
  - 자신의 월별 예상 정산 조회
  - 자신의 정산 생성 요청
- `Admin`
  - 생성된 정산 조회
  - 정산 확정
  - 정산 지급 처리

## 구현 시 주의사항
- 금액은 정수 원화 단위(`Long`)를 우선 사용한다.
- API 입력 시각은 `OffsetDateTime`으로 받더라도 내부 저장과 정산 계산은 KST 기준으로 통일한다.
- `Settlement`는 응답 DTO가 아니라 실제 관리 대상 엔티티이므로, 원천 데이터와 별도로 상태를 가진다.
- 중복 정산 방지는 애플리케이션 검증과 DB 유니크 제약을 함께 두는 편이 안전하다.
- 현재 수수료율은 `FeePolicy`, 변경 이력 조회는 `FeePolicyHistory`, 판매 당시 계산 기준은 `SaleRecord.feeRatePercent`로 역할을 분리한다.
- 수수료율 이력은 정산 계산의 직접 기준이 아니라 운영 변경 내역을 추적하기 위한 기록이다.
- 테스트는 판매/취소 원천 데이터와 정산 생성 이후 상태 전이 시나리오를 모두 검증할 수 있어야 한다.
