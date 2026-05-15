# API 명세

## 문서 목적
이 문서는 현재 프로젝트에서 구현할 HTTP API의 계약을 정리한다.  
과제 원문 기준 필수 API와 추가 요구로 반영한 인증 API를 함께 관리한다.

## 공통 규칙
- Base URL은 별도 버전 없이 사용한다.
- 요청/응답 포맷은 `application/json`을 사용한다.
- 시간 값은 ISO-8601 형식을 사용한다.
- 정산 기준 시간대는 KST(`Asia/Seoul`)이다.
- 금액 단위는 원이며 정수로 처리한다.
- 날짜 조회 파라미터는 KST 기준 비즈니스 날짜로 해석한다.

## API 성격 구분
- 예상 정산 조회 API
  - 원천 판매/취소 데이터를 기준으로 계산 결과만 조회한다.
  - 실제 `settlement` 테이블을 생성하거나 수정하지 않는다.
- 실제 정산 관리 API
  - `settlement` 테이블에 데이터를 생성하거나 상태를 변경한다.
  - `PENDING`, `CONFIRMED`, `PAID` 상태 관리에 직접 영향을 준다.

## 인증 규칙
- 로그인 API는 인증 없이 호출 가능하다.
- 판매 등록과 취소 등록 API는 현재 단계에서 인증 없이 호출 가능하다.
- 판매 내역 조회와 크리에이터 정산 조회는 `ROLE_CREATOR` 인증이 필요하다.
- 운영자 정산 조회는 `ROLE_ADMIN` 인증이 필요하다.
- 크리에이터 정산 생성과 자신의 생성 정산 조회는 `ROLE_CREATOR` 인증이 필요하다.
- 운영자 정산 확정, 지급 처리, 수수료율 이력 관리는 `ROLE_ADMIN` 인증이 필요하다.
- 판매 등록과 취소 등록은 이후 수강생 인증 체계가 추가되면 권한 정책을 확장할 수 있다.

## 공통 응답 형식
모든 API는 아래 공통 응답 형식을 사용한다.

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {}
}
```

## 응답 규칙
성공 응답 예시:

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {}
}
```

실패 응답 예시:

```json
{
  "errorCode": "AUTH_003",
  "msg": "인증이 필요합니다.",
  "data": null
}
```

## 1. 인증 API
추가 요구로 반영하는 API이다.

### 1.1 크리에이터 로그인
- Method: `POST`
- Path: `/creator/login`
- Auth: 없음

Request

```json
{
  "loginId": "creator1",
  "password": "password123"
}
```

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "accessToken": "jwt-token",
    "creatorId": "creator-1",
    "name": "김강사"
  }
}
```

### 1.2 운영자 로그인
- Method: `POST`
- Path: `/admin/login`
- Auth: 없음

Request

```json
{
  "loginId": "admin1",
  "password": "password123"
}
```

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "accessToken": "jwt-token",
    "adminId": "admin-1",
    "name": "운영자"
  }
}
```

## 1.5 강의 등록 API
테스트용 원천 데이터 입력을 위해 추가한 API이다.

### 1.5.1 강의 등록
- Method: `POST`
- Path: `/courses`
- Auth: 없음

Request

```json
{
  "courseId": "course-1",
  "creatorId": "creator-1",
  "title": "Spring Boot 입문",
  "registeredAt": "2025-03-01T00:00:00+09:00"
}
```

비고
- 이 API는 과제 검증용 데이터 입력 API이다.
- 이미 존재하는 `courseId`로 등록하면 중복 오류를 반환한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "courseId": "course-1"
  }
}
```

## 2. 판매 API
과제 원문 기준 필수 API이다.

### 2.1 판매 내역 등록
- Method: `POST`
- Path: `/sales`
- Auth: 없음

Request

```json
{
  "saleId": "sale-1",
  "courseId": "course-1",
  "studentId": "student-1",
  "amount": 50000,
  "paidAt": "2025-03-05T10:00:00+09:00"
}
```

비고
- 이 API는 과제 검증용 데이터 입력 API로 보고 `saleId`를 요청에서 직접 받는다.
- 이미 존재하는 `saleId`로 등록하면 중복 오류를 반환한다.
- 판매 등록 시 `paidAt` 이하에서 가장 최근의 `fee_policy_history.effective_started_at` 이력을 찾아 해당 시점의 수수료율을 `sale_record.fee_rate_percent`에 함께 저장한다.
- 운영자가 수수료율을 변경하면 변경 시점 이후의 `paidAt`을 가진 판매부터 변경된 수수료율을 사용한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "saleId": "sale-1"
  }
}
```

### 2.2 판매 내역 목록 조회
- Method: `GET`
- Path: `/sales`
- Auth: `ROLE_CREATOR`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `from` | `string` | Y | 조회 시작일, `yyyy-MM-dd`, KST 기준 |
| `to` | `string` | Y | 조회 종료일, `yyyy-MM-dd`, KST 기준 |

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "saleId": "sale-1",
      "courseId": "course-1",
      "studentId": "student-1",
      "amount": 50000,
      "feeRatePercent": 20,
      "paidAt": "2025-03-05T10:00:00+09:00"
    }
  ]
}
```

## 3. 취소 API
과제 원문 기준 필수 API이다.

### 3.1 취소 내역 등록
- Method: `POST`
- Path: `/sales/{saleId}/cancellations`
- Auth: 없음

Request

```json
{
  "cancelId": "cancel-1",
  "refundAmount": 80000,
  "canceledAt": "2025-03-25T18:00:00+09:00"
}
```

비고
- 이 API는 과제 검증용 데이터 입력 API로 보고 `cancelId`를 요청에서 직접 받는다.
- 이미 존재하는 `cancelId`로 등록하면 중복 오류를 반환한다.
- 같은 `saleId`에 대한 동시 취소 등록으로 누적 환불 검증이 깨지지 않도록, 원본 판매를 비관적 쓰기 락으로 먼저 조회한 뒤 같은 트랜잭션 안에서 환불 합계 검증과 저장을 수행한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "cancelId": "cancel-1"
  }
}
```

## 4. 크리에이터 정산 API
과제 원문 기준 필수 API이다.

### 4.1 월별 정산 조회
- Method: `GET`
- Path: `/creator/settlements/monthly`
- Auth: `ROLE_CREATOR`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `yearMonth` | `string` | Y | 조회 연월, 예: `2025-03` |

Response

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

비고
- 이 API는 예상 정산 조회 API이다.
- 조회 주체는 인증된 크리에이터 자신으로 제한한다.
- 판매는 `paidAt`, 취소는 `canceledAt` 기준으로 집계한다.
- 판매분은 각 판매에 저장된 `feeRatePercent` 기준으로 묶고, 환불분은 원본 판매의 `feeRatePercent` 기준으로 차감해 수수료율별 순액을 만든 뒤 계산한다.
- 모든 판매의 수수료율이 같으면 `플랫폼 수수료 = 순 판매 금액의 수수료율%` 공식과 동일한 결과가 된다.
- `platformFeeAmount`는 최소 0원이다.
- `expectedPayoutAmount`가 음수일 수 있으며, 이는 해당 정산에서 지급이 아니라 차감으로 처리되어야 할 금액이 발생했음을 의미한다.

## 5. 운영자 정산 API
과제 원문 기준 필수 API이다.

### 5.1 기간별 전체 정산 집계 조회
- Method: `GET`
- Path: `/admin/settlements`
- Auth: `ROLE_ADMIN`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `from` | `string` | Y | 조회 시작일, `yyyy-MM-dd`, KST 기준 |
| `to` | `string` | Y | 조회 종료일, `yyyy-MM-dd`, KST 기준 |

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "items": [
      {
        "creatorId": "creator-1",
        "creatorName": "김강사",
        "expectedPayoutAmount": 120000
      },
      {
        "creatorId": "creator-2",
        "creatorName": "이강사",
        "expectedPayoutAmount": 48000
      }
    ],
    "totalExpectedPayoutAmount": 168000
  }
}
```

비고
- 이 API는 예상 정산 조회 API이다.
- 실제 `settlement`를 조회하는 API가 아니라, 현재 원천 데이터 기준 예상 정산 합계를 계산해서 보여준다.
- `platformFeeAmount`는 최소 0원이다.
- `expectedPayoutAmount`가 음수일 수 있으며, 이는 해당 정산에서 지급이 아니라 차감으로 처리되어야 할 금액이 발생했음을 의미한다.

## 6. 추가 요구 정산 관리 API
추가 요구로 반영하는 API이다.

### 6.1 크리에이터 정산 생성
- Method: `POST`
- Path: `/creator/settlements`
- Auth: `ROLE_CREATOR`

Request

```json
{
  "yearMonth": "2025-03"
}
```

비고
- 이 API는 실제 정산 관리 API이다.
- 정산 대상 월이 종료된 이후에만 생성할 수 있다.
- 동일한 `creatorId + yearMonth` 조합의 정산은 하나만 생성할 수 있다.
- 생성 시 초기 상태는 `PENDING`이다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "settlementId": "settlement-1",
    "status": "PENDING"
  }
}
```

### 6.2 크리에이터 정산 목록 조회
- Method: `GET`
- Path: `/creator/settlements`
- Auth: `ROLE_CREATOR`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `from` | `string` | Y | 조회 시작 연월, 예: `2025-03` |
| `to` | `string` | Y | 조회 종료 연월, 예: `2025-05` |

비고
- 이 API는 실제 정산 관리 API이다.
- `from`, `to`는 정산 대상 연월 기준이며 양 끝 연월을 포함한다.
- 목록 응답에 정산 상세에 필요한 필드를 함께 내려주므로 별도 상세 조회 API는 두지 않는다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "settlementId": "settlement-1",
      "creatorId": "creator-1",
      "yearMonth": "2025-03",
      "grossSalesAmount": 260000,
      "refundAmount": 110000,
      "netSalesAmount": 150000,
      "platformFeeAmount": 30000,
      "expectedPayoutAmount": 120000,
      "saleCount": 4,
      "cancelCount": 2,
      "status": "PENDING",
      "requestedAt": "2025-04-01T09:00:00+09:00",
      "confirmedAt": null,
      "paidAt": null
    }
  ]
}
```

### 6.3 운영자 정산 목록 조회
- Method: `GET`
- Path: `/admin/settlement-management`
- Auth: `ROLE_ADMIN`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `from` | `string` | Y | 조회 시작 연월, 예: `2025-03` |
| `to` | `string` | Y | 조회 종료 연월, 예: `2025-05` |

비고
- 이 API는 실제 정산 관리 API이다.
- `from`, `to`는 정산 대상 연월 기준이며 양 끝 연월을 포함한다.
- 목록 응답에 정산 상세에 필요한 필드를 함께 내려주므로 별도 상세 조회 API는 두지 않는다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "settlementId": "settlement-1",
      "creatorId": "creator-1",
      "creatorName": "김강사",
      "yearMonth": "2025-03",
      "grossSalesAmount": 260000,
      "refundAmount": 110000,
      "netSalesAmount": 150000,
      "platformFeeAmount": 30000,
      "expectedPayoutAmount": 120000,
      "saleCount": 4,
      "cancelCount": 2,
      "status": "PENDING",
      "requestedAt": "2025-04-01T09:00:00+09:00",
      "confirmedAt": null,
      "paidAt": null
    }
  ]
}
```

### 6.4 운영자 정산 확정
- Method: `POST`
- Path: `/admin/settlement-management/{settlementId}/confirm`
- Auth: `ROLE_ADMIN`

비고
- 이 API는 실제 정산 관리 API이다.
- 대상 정산의 상태를 `PENDING -> CONFIRMED`로 변경한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "settlementId": "settlement-1",
    "status": "CONFIRMED"
  }
}
```

### 6.5 운영자 정산 지급 처리
- Method: `POST`
- Path: `/admin/settlement-management/{settlementId}/pay`
- Auth: `ROLE_ADMIN`

비고
- 이 API는 실제 정산 관리 API이다.
- 대상 정산의 상태를 `CONFIRMED -> PAID`로 변경한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "settlementId": "settlement-1",
    "status": "PAID"
  }
}
```

## 7. 추가 요구 수수료율 API
추가 요구로 반영하는 API이다.

### 7.0 사용 규칙
- `PATCH /admin/fee-policy`는 현재 시점부터 적용되는 운영 수수료율을 변경하는 API이다.
- 이 API를 호출하면 현재 수수료율(`fee_policy`)을 변경하고, 같은 시점의 변경 이력도 함께 기록한다.
- `POST /admin/fee-policy-histories`는 과거 또는 임의 시점의 수수료율 이력을 명시적으로 생성하기 위한 보조 API이다.
- 판매 등록은 `FeePolicy`의 현재값이 아니라, `paidAt` 이하에서 가장 최근의 `FeePolicyHistory`를 조회해 수수료율을 결정한다.
- 따라서 과거 시점 판매 데이터를 검증하려면 먼저 해당 시점의 수수료율 이력을 등록한 뒤 판매를 등록해야 한다.
- 반대로 현재 운영 수수료율을 변경하려는 목적이라면 `PATCH /admin/fee-policy`를 사용해야 하며, 같은 목적에 두 API를 혼용하지 않는다.

### 7.1 현재 수수료율 변경
- Method: `PATCH`
- Path: `/admin/fee-policy`
- Auth: `ROLE_ADMIN`

Request

```json
{
  "feeRatePercent": 20
}
```

비고
- `fee_policy`는 현재 적용할 수수료율을 저장하는 단일 row 설정이다.
- 요청과 응답의 `feeRatePercent`는 정수 퍼센트 값이며, 서버도 퍼센트 정수로 저장한다.
- 이 API는 현재 수수료율을 즉시 변경한다.
- 이 API 호출 시 현재 수수료율(`fee_policy`)을 변경하고, 같은 시점의 수수료율 변경 이력도 함께 기록한다.
- 변경 이후 시점의 `paidAt`을 가진 판매는 변경된 수수료율을 사용한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "feeRatePercent": 20
  }
}
```

### 7.2 수수료율 이력 등록
- Method: `POST`
- Path: `/admin/fee-policy-histories`
- Auth: `ROLE_ADMIN`

Request

```json
{
  "effectiveStartedAt": "2025-05-14T15:00:00+09:00",
  "feeRatePercent": 20
}
```

비고
- 요청과 응답의 `feeRatePercent`는 정수 퍼센트 값이며, 서버도 퍼센트 정수로 저장한다.
- 요청한 수수료율과 변경 적용 시각을 기준으로 `fee_policy_history`를 생성한다.
- 과거 시점 판매 테스트를 위해 별도 이력 등록 API를 제공한다.
- 판매 등록은 `paidAt` 이하에서 가장 최근의 수수료율 이력을 조회해 수수료율을 결정한다.
- 이미 저장된 판매의 `sale_record.fee_rate_percent`는 이후 이력 등록으로 변경되지 않는다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "feePolicyHistoryId": "fee-policy-history-1",
    "effectiveStartedAt": "2025-05-14T15:00:00+09:00",
    "feeRatePercent": 20
  }
}
```

### 7.3 수수료율 이력 목록 조회
- Method: `GET`
- Path: `/admin/fee-policy-histories`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "feePolicyHistoryId": "fee-policy-history-1",
      "effectiveStartedAt": "2025-05-14T15:00:00+09:00",
      "feeRatePercent": 20
    }
  ]
}
```

## 8. 에러 코드
공통 응답의 `errorCode`는 아래 정의된 값을 사용한다.

입력값 누락, 형식 오류, Bean Validation 실패는 개별 도메인 에러코드로 분리하지 않고 `COMMON_001`로 처리한다.

### AUTH

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `AUTH_001` | `401` | 유효하지 않은 액세스 토큰 |
| `AUTH_002` | `401` | 만료된 액세스 토큰 |
| `AUTH_003` | `401` | 인증이 필요함 |
| `AUTH_004` | `403` | 접근 권한이 없음 |
| `AUTH_005` | `401` | 크리에이터 로그인 ID 또는 비밀번호 불일치 |
| `AUTH_006` | `401` | 운영자 로그인 ID 또는 비밀번호 불일치 |
| `AUTH_007` | `404` | 크리에이터를 찾을 수 없음 |

### COURSE

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `COURSE_001` | `404` | 강의를 찾을 수 없음 |
| `COURSE_002` | `409` | 이미 존재하는 강의 ID |

### SALE

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `SALE_001` | `404` | 판매 내역을 찾을 수 없음 |
| `SALE_002` | `409` | 이미 존재하는 판매 ID |
| `SALE_003` | `422` | 결제 일시가 강의 등록 일시보다 빠를 수 없음 |

### CANCELLATION

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `CANCELLATION_001` | `409` | 이미 존재하는 취소 ID |
| `CANCELLATION_002` | `422` | 취소 일시가 원본 판매의 결제 일시보다 빠를 수 없음 |
| `CANCELLATION_003` | `422` | 환불 금액이 원 결제 금액을 초과함 |

### SETTLEMENT

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `SETTLEMENT_001` | `409` | 동일 연월 정산이 이미 존재함 |
| `SETTLEMENT_002` | `400` | 대상 월이 종료되기 전에는 정산을 생성할 수 없음 |
| `SETTLEMENT_003` | `404` | 정산 정보를 찾을 수 없음 |
| `SETTLEMENT_004` | `409` | 현재 상태에서는 정산 확정을 할 수 없음 |
| `SETTLEMENT_005` | `409` | 현재 상태에서는 정산 지급 처리를 할 수 없음 |

### FEE_POLICY

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `FEE_POLICY_001` | `404` | 수수료율 정책을 찾을 수 없음 |
| `FEE_POLICY_002` | `409` | 동일한 변경 기준 시각의 수수료율 이력이 이미 존재함 |

### COMMON

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `COMMON_001` | `400` | 요청 값이 올바르지 않음 |
| `COMMON_002` | `500` | 서버 내부 오류 |
| `COMMON_003` | `404` | 존재하지 않는 엔드포인트 |
| `COMMON_004` | `405` | 허용되지 않은 HTTP 메서드 |
| `COMMON_005` | `400` | 조회 시작 값은 조회 종료 값보다 늦을 수 없음 |

## 9. 구현 메모
- `Controller -> UseCase -> Service -> Repository` 구조를 따른다.
- `Controller`는 `Request`, `UseCase`는 `Response`를 기준으로 구현하고, `Service`는 웹 계층 DTO를 직접 의존하지 않는다.
- `Service`는 다른 `Service`를 의존하지 않고, 여러 작업 조합은 `UseCase`에서 처리한다.
- 유니크 제약이 있는 생성 API는 사전 조회만으로 끝내지 않고, 저장 시점의 DB 유니크 충돌도 각 도메인 비즈니스 예외로 변환한다.
- 취소 등록은 `CancellationRecord` 합계를 직접 잠그지 않고, 기준 자원인 `SaleRecord`를 비관적 쓰기 락으로 보호해 누적 환불 검증과 저장을 직렬화한다.
