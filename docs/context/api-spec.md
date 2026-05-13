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

## 2. 판매 API
과제 원문 기준 필수 API이다.

### 2.1 판매 내역 등록
- Method: `POST`
- Path: `/sales`
- Auth: 없음

Request

```json
{
  "courseId": "course-1",
  "studentId": "student-1",
  "amount": 50000,
  "paidAt": "2025-03-05T10:00:00+09:00"
}
```

비고
- `saleId`는 요청으로 받지 않고 서버에서 생성한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "판매 내역이 등록되었습니다.",
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
| `from` | `string` | N | 조회 시작일시 |
| `to` | `string` | N | 조회 종료일시 |

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
  "refundAmount": 80000,
  "canceledAt": "2025-03-25T18:00:00+09:00"
}
```

비고
- `cancelId`는 요청으로 받지 않고 서버에서 생성한다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "취소 내역이 등록되었습니다.",
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
- 조회 주체는 인증된 크리에이터 자신으로 제한한다.
- 판매는 `paidAt`, 취소는 `canceledAt` 기준으로 집계한다.

## 5. 운영자 정산 API
과제 원문 기준 필수 API이다.

### 5.1 기간별 전체 정산 집계 조회
- Method: `GET`
- Path: `/admin/settlements`
- Auth: `ROLE_ADMIN`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `from` | `string` | Y | 조회 시작일시 |
| `to` | `string` | Y | 조회 종료일시 |

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
- 정산 대상 월이 종료된 이후에만 생성할 수 있다.
- 동일한 `creatorId + yearMonth` 조합의 정산은 하나만 생성할 수 있다.
- 생성 시 초기 상태는 `PENDING`이다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "정산이 생성되었습니다.",
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
| `status` | `string` | N | `PENDING`, `CONFIRMED`, `PAID` |
| `yearMonth` | `string` | N | 조회 연월, 예: `2025-03` |

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "settlementId": "settlement-1",
      "yearMonth": "2025-03",
      "expectedPayoutAmount": 120000,
      "status": "PENDING",
      "requestedAt": "2025-04-01T09:00:00+09:00"
    }
  ]
}
```

### 6.3 크리에이터 정산 상세 조회
- Method: `GET`
- Path: `/creator/settlements/{settlementId}`
- Auth: `ROLE_CREATOR`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "settlementId": "settlement-1",
    "creatorId": "creator-1",
    "yearMonth": "2025-03",
    "platformFeeRate": 0.2,
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
}
```

### 6.4 운영자 정산 목록 조회
- Method: `GET`
- Path: `/admin/settlement-management`
- Auth: `ROLE_ADMIN`

Query Parameters

| 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `status` | `string` | N | `PENDING`, `CONFIRMED`, `PAID` |
| `yearMonth` | `string` | N | 정산 대상 연월 |
| `creatorId` | `string` | N | 특정 크리에이터 필터 |

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
      "expectedPayoutAmount": 120000,
      "status": "PENDING"
    }
  ]
}
```

### 6.5 운영자 정산 상세 조회
- Method: `GET`
- Path: `/admin/settlement-management/{settlementId}`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "settlementId": "settlement-1",
    "creatorId": "creator-1",
    "creatorName": "김강사",
    "yearMonth": "2025-03",
    "platformFeeRate": 0.2,
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
}
```

### 6.6 운영자 정산 확정
- Method: `POST`
- Path: `/admin/settlement-management/{settlementId}/confirm`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "정산이 확정되었습니다.",
  "data": {
    "settlementId": "settlement-1",
    "status": "CONFIRMED"
  }
}
```

### 6.7 운영자 정산 지급 처리
- Method: `POST`
- Path: `/admin/settlement-management/{settlementId}/pay`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "정산이 지급 완료되었습니다.",
  "data": {
    "settlementId": "settlement-1",
    "status": "PAID"
  }
}
```

## 7. 추가 요구 수수료율 이력 API
추가 요구로 반영하는 API이다.

### 7.1 수수료율 이력 등록
- Method: `POST`
- Path: `/admin/fee-policies`
- Auth: `ROLE_ADMIN`

Request

```json
{
  "targetYearMonth": "2025-03",
  "feeRate": 0.2
}
```

비고
- 한 연월에는 하나의 수수료율만 등록할 수 있다.

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "수수료율 이력이 등록되었습니다.",
  "data": {
    "feePolicyHistoryId": "fee-policy-1"
  }
}
```

### 7.2 수수료율 이력 목록 조회
- Method: `GET`
- Path: `/admin/fee-policies`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": [
    {
      "feePolicyHistoryId": "fee-policy-1",
      "targetYearMonth": "2025-03",
      "feeRate": 0.2
    }
  ]
}
```

### 7.3 수수료율 이력 상세 조회
- Method: `GET`
- Path: `/admin/fee-policies/{feePolicyHistoryId}`
- Auth: `ROLE_ADMIN`

Response

```json
{
  "errorCode": "SUCCESS",
  "msg": "ok",
  "data": {
    "feePolicyHistoryId": "fee-policy-1",
    "targetYearMonth": "2025-03",
    "feeRate": 0.2
  }
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
| `AUTH_008` | `404` | 운영자 계정을 찾을 수 없음 |

### COURSE

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `COURSE_001` | `404` | 강의를 찾을 수 없음 |
| `COURSE_002` | `403` | 해당 크리에이터가 소유한 강의가 아님 |

### SALE

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `SALE_001` | `404` | 판매 내역을 찾을 수 없음 |
| `SALE_005` | `403` | 자신의 판매 내역만 조회할 수 있음 |

### CANCELLATION

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `CANCELLATION_001` | `404` | 취소 대상 판매 내역을 찾을 수 없음 |
| `CANCELLATION_004` | `422` | 환불 금액이 원 결제 금액을 초과함 |

### SETTLEMENT

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `SETTLEMENT_003` | `400` | 조회 시작일은 종료일보다 늦을 수 없음 |
| `SETTLEMENT_004` | `403` | 자신의 정산 정보만 조회할 수 있음 |
| `SETTLEMENT_005` | `404` | 정산 대상 크리에이터를 찾을 수 없음 |
| `SETTLEMENT_006` | `409` | 동일 연월 정산이 이미 존재함 |
| `SETTLEMENT_007` | `400` | 대상 월이 종료되기 전에는 정산을 생성할 수 없음 |
| `SETTLEMENT_008` | `404` | 정산 정보를 찾을 수 없음 |
| `SETTLEMENT_009` | `409` | 현재 상태에서는 정산 확정을 할 수 없음 |
| `SETTLEMENT_010` | `409` | 현재 상태에서는 정산 지급 처리를 할 수 없음 |

### FEE_POLICY

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `FEE_POLICY_001` | `404` | 수수료율 이력을 찾을 수 없음 |
| `FEE_POLICY_002` | `409` | 해당 연월의 수수료율 이력이 이미 존재함 |

### COMMON

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `COMMON_001` | `400` | 요청 값이 올바르지 않음 |
| `COMMON_002` | `500` | 서버 내부 오류 |
| `COMMON_003` | `404` | 존재하지 않는 엔드포인트 |
| `COMMON_004` | `405` | 허용되지 않은 HTTP 메서드 |

## 9. 구현 메모
- `Controller -> UseCase -> Service -> Repository` 구조를 따른다.
- `Controller`는 `Request`, `UseCase`는 `Response`를 기준으로 구현하고, `Service`는 웹 계층 DTO를 직접 의존하지 않는다.
- `Service`는 다른 `Service`를 의존하지 않고, 여러 작업 조합은 `UseCase`에서 처리한다.
