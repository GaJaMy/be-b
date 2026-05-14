# 크리에이터 정산 API

## 프로젝트 개요
이 프로젝트는 크리에이터 강의 판매 데이터를 기반으로 정산 금액을 계산하고 관리하는 백엔드 과제 프로젝트이다.

핵심 기능은 다음과 같다.
- 판매 내역 등록
- 취소 내역 등록
- 크리에이터 월별 예상 정산 조회
- 운영자 기간별 예상 정산 집계 조회
- 실제 정산 생성 및 상태 관리 (`PENDING -> CONFIRMED -> PAID`)
- 현재 수수료율 변경 및 수수료율 변경 이력 관리

예상 정산 조회 API와 실제 정산 관리 API를 분리한 것이 이 프로젝트의 중요한 특징이다.

## 기술 스택
- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Security
- Spring Data JPA
- MySQL
- Flyway
- Spring Validation
- JWT (`jjwt`)
- Springdoc OpenAPI / Swagger UI
- Gradle

## 실행 방법
추후 보완 예정

## 요구사항 해석 및 가정
- 판매는 `paidAt`, 취소는 `canceledAt` 기준으로 해당 월에 반영한다.
- 시간대 기준은 KST(`Asia/Seoul`)이다.
- API 입력 시각이 `OffsetDateTime`이어도 내부 저장과 계산은 KST 기준 `LocalDateTime`으로 정규화한다.
- 예상 정산 조회 API는 실제 `settlement` 데이터를 생성하거나 변경하지 않는다.
- 실제 정산은 크리에이터가 정산 생성 API를 호출했을 때 생성된다.
- 동일한 `creatorId + yearMonth`에 대해서는 하나의 `Settlement`만 생성 가능하다고 가정한다.
- 수수료율 변경은 변경 시점부터 즉시 적용된다고 해석했다.
- 과거 판매의 정산 계산 기준은 판매 등록 당시 `SaleRecord.feeRatePercent`에 저장된 스냅샷 수수료율이다.
- 취소는 취소 월에 반영하지만, 수수료율은 원본 판매의 수수료율을 따른다.
- 플랫폼 수수료는 음수가 될 수 없으므로 최소 0원으로 처리한다.
- 정산 예정 금액이 음수면, 해당 정산에서 직접 반영되는 차감 조정 금액으로 해석한다.

## 설계 결정과 이유
- 예상 정산 조회와 실제 정산 관리를 분리했다.
  - `/creator/settlements/monthly`, `/admin/settlements`는 원천 데이터 기반 계산 조회이다.
  - 실제 정산 상태 관리는 `Settlement` 엔티티로 별도 관리한다.
- 판매 내역에 수수료율 스냅샷을 저장했다.
  - 수수료율이 나중에 바뀌어도 과거 판매 정산 기준이 변하지 않도록 하기 위함이다.
- 취소 수수료 계산은 현재 수수료율이 아니라 원본 판매 수수료율을 사용한다.
  - “과거 정산은 당시 수수료율 적용” 요구를 가장 자연스럽게 만족시키기 위한 결정이다.
- `FeePolicy`와 `FeePolicyHistory`를 분리했다.
  - `FeePolicy`는 현재 적용 수수료율
  - `FeePolicyHistory`는 운영자가 언제 어떤 값으로 변경했는지 남기는 이력
- 실제 정산 생성은 월 종료 후에만 가능하도록 설계했다.
  - 월 중간에는 예상 정산만 조회하고, 실제 정산은 월 마감 후 생성하는 흐름을 택했다.
- 수수료는 수수료율별 순액 합산 방식으로 계산한다.
  - 모든 판매의 수수료율이 같으면 과제 원문의 단순 공식과 동일한 결과가 나온다.

## 미구현 / 제약사항
- README 기준 실행 방법은 아직 정리하지 않았다.
- 테스트 코드는 아직 본격적으로 작성하지 않았고, 현재는 Flyway 시드와 시나리오 검증 문서 중심으로 검증한다.
- 음수 정산 예정 금액의 후속 처리(다음 정산 이월, 자동 상계, 별도 회수)는 구현 범위에 포함하지 않았다.
- 운영 환경 배포 설정, CI, 모니터링 등은 과제 범위 밖으로 두었다.

## AI 활용 범위
- OpenAI Codex를 사용해 문서 작성, 설계 정리, 코드 보조 작업을 진행했다.
- 주요 활용 범위:
  - 프로젝트 컨텍스트 문서화
  - ERD 및 도메인 설계 정리
  - API 명세 정리
  - Swagger 명세 보강
  - 시나리오 시드 및 검증 문서 구성
- 최종 정책 결정과 설계 해석은 프로젝트 문서 기준으로 직접 정리했다.

## API 목록 및 예시
### 인증 API
- `POST /creator/login`
- `POST /admin/login`

예시:

```json
{
  "loginId": "creator-1",
  "password": "qwe123123!"
}
```

### 판매 / 취소 API
- `POST /sales`
- `GET /sales?from=2025-03-01&to=2025-03-31`
- `POST /sales/{saleId}/cancellations`

판매 등록 예시:

```json
{
  "saleId": "sale-1",
  "courseId": "course-1",
  "studentId": "student-1",
  "amount": 50000,
  "paidAt": "2025-03-05T10:00:00+09:00"
}
```

취소 등록 예시:

```json
{
  "cancelId": "cancel-1",
  "refundAmount": 30000,
  "canceledAt": "2025-03-28T15:00:00+09:00"
}
```

### 예상 정산 조회 API
- `GET /creator/settlements/monthly?yearMonth=2025-03`
- `GET /admin/settlements?from=2025-03-01&to=2025-03-31`

크리에이터 월별 예상 정산 응답 예시:

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "creatorId": "creator-1",
    "yearMonth": "2025-03",
    "grossSalesAmount": 260000,
    "refundAmount": 110000,
    "netSalesAmount": 150000,
    "platformFeeAmount": 30000,
    "expectedPayoutAmount": 120000,
    "saleCount": 4,
    "cancelCount": 2
  }
}
```

### 실제 정산 관리 API
- `POST /creator/settlements`
- `GET /creator/settlements?from=2025-03&to=2025-05`
- `GET /admin/settlement-management?from=2025-03&to=2025-05`
- `POST /admin/settlement-management/{settlementId}/confirm`
- `POST /admin/settlement-management/{settlementId}/pay`

정산 생성 요청 예시:

```json
{
  "yearMonth": "2025-03"
}
```

### 수수료율 API
- `PATCH /admin/fee-policy`
- `POST /admin/fee-policy-histories`
- `GET /admin/fee-policy-histories`

수수료율 이력 등록 예시:

```json
{
  "effectiveStartedAt": "2025-05-10T00:00:00+09:00",
  "feeRatePercent": 30
}
```

자세한 계약은 [docs/context/api-spec.md](docs/context/api-spec.md)를 기준으로 본다.

## 데이터 모델 설명
### Creator
- 크리에이터 계정
- 강의, 판매, 취소, 정산의 소유 주체

### Admin
- 운영자 계정
- 정산 확정, 지급 처리, 수수료율 변경의 주체

### Course
- 크리에이터가 소유한 강의

### SaleRecord
- 판매 원천 데이터
- `amount`, `paidAt`, `feeRatePercent`를 가진다.
- `feeRatePercent`는 판매 당시 수수료율 스냅샷이다.

### CancellationRecord
- 취소 원천 데이터
- 원본 `SaleRecord`를 참조한다.
- `refundAmount`, `canceledAt`을 가진다.

### Settlement
- 실제 생성된 정산 데이터
- 금액 스냅샷과 상태(`PENDING`, `CONFIRMED`, `PAID`)를 가진다.

### FeePolicy
- 현재 적용 중인 수수료율

### FeePolicyHistory
- 수수료율 변경 이력
- `effectiveStartedAt` 기준으로 언제부터 어떤 수수료율이 적용되었는지 기록한다.

자세한 도메인 설명은 아래 문서를 기준으로 본다.
- [docs/context/domain-design.md](docs/context/domain-design.md)
- [docs/context/erd.md](docs/context/erd.md)

## 테스트 실행 방법
정식 자동화 테스트 코드는 추후 보완 예정이며, 현재는 아래 방식으로 검증한다.

- Flyway 시드(`V2`, `V4`)를 적용한다.
- Swagger 또는 API 클라이언트로 시나리오별 API를 호출한다.
- 기대 결과는 [docs/scenario-verification-guide.md](docs/scenario-verification-guide.md) 문서를 기준으로 확인한다.

검증 문서에는 다음이 정리되어 있다.
- 어떤 계정으로 로그인해야 하는지
- 어떤 API를 호출해야 하는지
- 어떤 판매/취소 데이터가 반영되는지
- 어떤 계산 과정을 거쳐 어떤 결과가 나와야 하는지
