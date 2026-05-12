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
- 크리에이터 API는 `ROLE_CREATOR` 인증이 필요하다.
- 운영자 API는 `ROLE_ADMIN` 인증이 필요하다.
- 로그인 API는 인증 없이 호출 가능하다.

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
  "errorCode": "AUTH_004",
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
- Auth: `ROLE_ADMIN`

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
- Auth: `ROLE_ADMIN`

Request

```json
{
  "cancelId": "cancel-1",
  "refundAmount": 80000,
  "canceledAt": "2025-03-25T18:00:00+09:00"
}
```

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

## 6. 에러 코드
공통 응답의 `errorCode`는 아래 정의된 값을 사용한다.

### AUTH

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `AUTH_001` | `401` | 유효하지 않은 액세스 토큰 |
| `AUTH_002` | `401` | 만료된 액세스 토큰 |
| `AUTH_003` | `401` | 인증이 필요함 |
| `AUTH_004` | `403` | 접근 권한이 없음 |
| `AUTH_005` | `401` | 크리에이터 로그인 ID 또는 비밀번호 불일치 |
| `AUTH_006` | `401` | 운영자 로그인 ID 또는 비밀번호 불일치 |
| `AUTH_007` | `404` | 크리에이터 계정을 찾을 수 없음 |
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
| `SALE_002` | `409` | 이미 존재하는 판매 ID |
| `SALE_003` | `400` | 결제 금액은 0보다 커야 함 |
| `SALE_004` | `400` | 결제 일시 형식이 올바르지 않음 |
| `SALE_005` | `403` | 자신의 판매 내역만 조회할 수 있음 |

### CANCELLATION

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `CANCELLATION_001` | `404` | 취소 대상 판매 내역을 찾을 수 없음 |
| `CANCELLATION_002` | `409` | 이미 존재하는 취소 ID |
| `CANCELLATION_003` | `400` | 환불 금액은 0보다 커야 함 |
| `CANCELLATION_004` | `422` | 환불 금액이 원 결제 금액을 초과함 |
| `CANCELLATION_005` | `400` | 취소 일시 형식이 올바르지 않음 |

### SETTLEMENT

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `SETTLEMENT_001` | `400` | 조회 연월 형식이 올바르지 않음 |
| `SETTLEMENT_002` | `400` | 조회 시작일 또는 종료일 형식이 올바르지 않음 |
| `SETTLEMENT_003` | `400` | 조회 시작일은 종료일보다 늦을 수 없음 |
| `SETTLEMENT_004` | `403` | 자신의 정산 정보만 조회할 수 있음 |
| `SETTLEMENT_005` | `404` | 정산 대상 크리에이터를 찾을 수 없음 |

### COMMON

| 코드 | HTTP | 설명 |
| --- | --- | --- |
| `COMMON_001` | `400` | 요청 값이 올바르지 않음 |
| `COMMON_002` | `500` | 서버 내부 오류 |

## 7. 구현 메모
- `Controller -> UseCase -> Service -> Repository` 구조를 따른다.
- `Controller`는 `Request`, `UseCase`는 `Response`, `Service`는 `Command`와 `Result`를 기준으로 구현한다.
- `Service`는 다른 `Service`를 의존하지 않고, 여러 작업 조합은 `UseCase`에서 처리한다.
