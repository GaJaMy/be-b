# 시나리오 검증 가이드

## 문서 목적
이 문서는 Flyway 시드 `V4__scenario_test_seed.sql`에 포함된 계산 시나리오를 채점자가 빠르게 검증할 수 있도록 정리한 안내서이다.  
각 시나리오마다 어떤 판매/취소 데이터가 들어가 있는지, 어떤 API를 호출해야 하는지, 어떤 계산 과정을 거쳐 어떤 값이 나와야 하는지를 함께 적는다.

## 공통 정보
- 로그인 비밀번호: `qwe123123!`
- 크리에이터 로그인 API: `POST /creator/login`
- 운영자 로그인 API: `POST /admin/login`
- 예상 정산 조회 API: `GET /creator/settlements/monthly?yearMonth=YYYY-MM`
- 실제 정산 상태 조회 API: `GET /creator/settlements?from=YYYY-MM&to=YYYY-MM`
- 운영자 예상 정산 조회 API: `GET /admin/settlements?from=YYYY-MM-dd&to=YYYY-MM-dd`
- 계산 공통 규칙:
  - 총 판매 금액 = 해당 월 `paidAt` 기준 판매 금액 합계
  - 환불 금액 = 해당 월 `canceledAt` 기준 환불 금액 합계
  - 순 판매 금액 = 총 판매 금액 - 환불 금액
  - 플랫폼 수수료 = 수수료율별 순액에 대한 수수료 합계
  - 정산 예정 금액 = 순 판매 금액 - 플랫폼 수수료
  - 플랫폼 수수료는 음수가 될 수 없으므로 최소 0원으로 처리

## 기본 검증 절차
### 1. 로그인
크리에이터 시나리오는 먼저 로그인해서 access token을 발급받는다.

Request

```json
{
  "loginId": "creator_scenario_1",
  "password": "qwe123123!"
}
```

확인 포인트
- 응답 `data.accessToken`을 이후 요청의 `Authorization: Bearer {token}` 헤더에 사용한다.

### 2. 월별 예상 정산 조회
예상 정산 시나리오는 아래 형식으로 조회한다.

```http
GET /creator/settlements/monthly?yearMonth=2025-04
Authorization: Bearer {creator-access-token}
```

확인 포인트
- 응답의 `grossSalesAmount`
- 응답의 `refundAmount`
- 응답의 `netSalesAmount`
- 응답의 `platformFeeAmount`
- 응답의 `expectedPayoutAmount`
- 응답의 `saleCount`, `cancelCount`

### 3. 실제 정산 상태 조회
실제 정산 상태 시나리오는 아래 형식으로 조회한다.

```http
GET /creator/settlements?from=2025-01&to=2025-03
Authorization: Bearer {creator-access-token}
```

확인 포인트
- 목록 건수
- 각 항목의 `status`
- `yearMonth` 정렬과 포함 범위

## 수수료율 이력 기준
아래 이력은 전역 현재 수수료율 변경 이력이다. 각 판매의 `fee_rate_percent`는 판매 시점에 유효한 이력값과 일치하도록 시드를 구성했다.

| 적용 시작 시각 | 수수료율 |
| --- | --- |
| `2025-04-01 00:00:00` | `20%` |
| `2025-04-15 00:00:00` | `25%` |
| `2025-05-10 00:00:00` | `30%` |
| `2025-06-01 00:00:00` | `35%` |
| `2025-07-10 00:00:00` | `40%` |
| `2025-07-20 00:00:00` | `45%` |
| `2025-08-01 00:00:00` | `20%` |

비고
- `V3`의 현재 `fee_policy.fee_rate_percent`는 `20`으로 시작하고, `V4`의 마지막 이력도 `2025-08-01 / 20%`로 맞춰진다.
- 기존 계산 시나리오는 모두 `2025-07` 이전 데이터만 사용하므로, 마지막 `2025-08-01` 이력은 기존 검증 결과에 영향을 주지 않는다.

## 시나리오 1. 무료 강의 / 무료 환불
- 계정: `creator_scenario_1`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-04`
- 검증 순서:
  - `creator_scenario_1 / qwe123123!`로 로그인
  - 발급받은 access token으로 `GET /creator/settlements/monthly?yearMonth=2025-04` 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-1`: `amount = 0`, `feeRatePercent = 20`, `paidAt = 2025-04-05 10:00:00`
  - 취소
    - `cancel-scenario-1`: `refundAmount = 0`, `canceledAt = 2025-04-06 10:00:00`
- 계산:
  - 총 판매 금액 = `0`
  - 환불 금액 = `0`
  - 순 판매 금액 = `0 - 0 = 0`
  - 플랫폼 수수료 = `0`
  - 정산 예정 금액 = `0 - 0 = 0`
- 기대 결과:
  - `grossSalesAmount = 0`
  - `refundAmount = 0`
  - `netSalesAmount = 0`
  - `platformFeeAmount = 0`
  - `expectedPayoutAmount = 0`
- 확인 포인트:
  - 무료 판매와 무료 환불도 건수에는 반영되지만 금액 계산은 모두 0으로 유지되는지 확인한다.

## 시나리오 2. 동일 판매 다중 취소 누적
- 계정: `creator_scenario_2`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-04`
- 검증 순서:
  - `creator_scenario_2 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-04` 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-2`: `amount = 100000`, `feeRatePercent = 20`, `paidAt = 2025-04-10 09:00:00`
  - 취소
    - `cancel-scenario-2`: `refundAmount = 30000`, `canceledAt = 2025-04-11 10:00:00`
    - `cancel-scenario-3`: `refundAmount = 20000`, `canceledAt = 2025-04-12 11:00:00`
- 계산:
  - 총 판매 금액 = `100000`
  - 환불 금액 = `30000 + 20000 = 50000`
  - 순 판매 금액 = `100000 - 50000 = 50000`
  - 수수료율별 순액
    - `20%` 그룹: `100000 - 30000 - 20000 = 50000`
  - 플랫폼 수수료 = `50000 * 20% = 10000`
  - 정산 예정 금액 = `50000 - 10000 = 40000`
- 기대 결과:
  - `grossSalesAmount = 100000`
  - `refundAmount = 50000`
  - `netSalesAmount = 50000`
  - `platformFeeAmount = 10000`
  - `expectedPayoutAmount = 40000`
- 확인 포인트:
  - 하나의 판매에 연결된 여러 취소가 모두 같은 월 환불 금액으로 합산되는지 확인한다.

## 시나리오 3. 월 경계 판매 / 취소
- 계정: `creator_scenario_3`
- 호출:
  - `GET /creator/settlements/monthly?yearMonth=2025-04`
  - `GET /creator/settlements/monthly?yearMonth=2025-05`
- 검증 순서:
  - `creator_scenario_3 / qwe123123!`로 로그인
  - 4월 조회 후 5월 조회를 순서대로 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-3`: `amount = 70000`, `feeRatePercent = 25`, `paidAt = 2025-04-30 23:59:00`
  - 취소
    - `cancel-scenario-4`: `refundAmount = 70000`, `canceledAt = 2025-05-01 00:01:00`
- 4월 계산:
  - 총 판매 금액 = `70000`
  - 환불 금액 = `0`
  - 순 판매 금액 = `70000`
  - 플랫폼 수수료 = `70000 * 25% = 17500`
  - 정산 예정 금액 = `70000 - 17500 = 52500`
- 5월 계산:
  - 총 판매 금액 = `0`
  - 환불 금액 = `70000`
  - 순 판매 금액 = `-70000`
  - 플랫폼 수수료 = `0`
  - 정산 예정 금액 = `-70000`
- 기대 결과:
  - 4월은 판매만 반영
  - 5월은 취소만 반영
- 확인 포인트:
  - `paidAt`과 `canceledAt`이 서로 다른 월이면 각 월에 분리 반영되는지 확인한다.

## 시나리오 4. 서로 다른 수수료율 판매 혼합
- 계정: `creator_scenario_4`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-05`
- 검증 순서:
  - `creator_scenario_4 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-05` 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-4`: `amount = 50000`, `feeRatePercent = 25`, `paidAt = 2025-05-03 12:00:00`
    - `sale-scenario-5`: `amount = 50000`, `feeRatePercent = 30`, `paidAt = 2025-05-20 12:00:00`
  - 취소
    - `cancel-scenario-5`: `refundAmount = 10000`, `saleRecordId = sale-scenario-5`, `canceledAt = 2025-05-21 09:00:00`
- 계산:
  - 총 판매 금액 = `50000 + 50000 = 100000`
  - 환불 금액 = `10000`
  - 순 판매 금액 = `100000 - 10000 = 90000`
  - 수수료율별 순액
    - `25%` 그룹: `50000`
    - `30%` 그룹: `50000 - 10000 = 40000`
  - 플랫폼 수수료 = `50000 * 25% + 40000 * 30% = 12500 + 12000 = 24500`
  - 정산 예정 금액 = `90000 - 24500 = 65500`
- 기대 결과:
  - `grossSalesAmount = 100000`
  - `refundAmount = 10000`
  - `netSalesAmount = 90000`
  - `platformFeeAmount = 24500`
  - `expectedPayoutAmount = 65500`
- 확인 포인트:
  - 단일 수수료율이 아니라 수수료율 그룹별 순액 합산으로 수수료가 계산되는지 확인한다.

## 시나리오 5. 실제 정산 상태 조회
- 계정: `creator_scenario_5`
- 호출: `GET /creator/settlements?from=2025-01&to=2025-03`
- 검증 순서:
  - `creator_scenario_5 / qwe123123!`로 로그인
  - `GET /creator/settlements?from=2025-01&to=2025-03` 호출
- 원천 데이터:
  - `settlement-scenario-1`: `2025-01`, `PENDING`
  - `settlement-scenario-2`: `2025-02`, `CONFIRMED`
  - `settlement-scenario-3`: `2025-03`, `PAID`
- 기대 결과:
  - 목록에 3건이 조회
  - 상태가 각각 `PENDING`, `CONFIRMED`, `PAID`
- 확인 포인트:
  - 실제 정산 관리 API와 예상 정산 조회 API가 분리되어 있음을 함께 확인한다.

## 시나리오 6. 과거 판매 수수료율 유지 + 같은 월 신규 판매
- 계정: `creator_scenario_6`
- 호출:
  - `GET /creator/settlements/monthly?yearMonth=2025-05`
  - `GET /creator/settlements/monthly?yearMonth=2025-06`
- 검증 순서:
  - `creator_scenario_6 / qwe123123!`로 로그인
  - 5월 조회 후 6월 조회를 순서대로 호출
- 원천 데이터:
  - 4월 판매
    - `sale-scenario-6`: `50000`, `20%`, `paidAt = 2025-04-10 13:00:00`
    - `sale-scenario-7`: `80000`, `25%`, `paidAt = 2025-04-20 15:00:00`
  - 5월 판매
    - `sale-scenario-8`: `40000`, `30%`, `paidAt = 2025-05-15 14:00:00`
  - 5월 취소
    - `cancel-scenario-6`: `10000`, 원본 판매 `sale-scenario-6`, `canceledAt = 2025-05-10 10:00:00`
  - 6월 취소
    - `cancel-scenario-7`: `20000`, 원본 판매 `sale-scenario-7`, `canceledAt = 2025-06-10 10:00:00`
- 5월 계산:
  - 총 판매 금액 = `40000`
  - 환불 금액 = `10000`
  - 순 판매 금액 = `30000`
  - 수수료율별 순액
    - `20%` 그룹: `-10000`
    - `30%` 그룹: `40000`
  - 플랫폼 수수료 = `0 + (40000 * 30%) = 12000`
  - 정산 예정 금액 = `30000 - 12000 = 18000`
- 6월 계산:
  - 총 판매 금액 = `0`
  - 환불 금액 = `20000`
  - 순 판매 금액 = `-20000`
  - 플랫폼 수수료 = `0`
  - 정산 예정 금액 = `-20000`
- 의미:
  - 5월 / 6월의 현재 수수료율이 달라도 취소는 원본 판매 수수료율을 따른다.
- 확인 포인트:
  - 5월 환불은 `sale-scenario-6`의 `20%`
  - 6월 환불은 `sale-scenario-7`의 `25%`
  를 따라가며, 조회 시점 현재 수수료율과 무관하게 계산되는지 확인한다.

## 시나리오 7. 환불만 있는 월
- 계정: `creator_scenario_7`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-05`
- 검증 순서:
  - `creator_scenario_7 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-05` 호출
- 원천 데이터:
  - 4월 판매
    - `sale-scenario-9`: `30000`, `20%`, `paidAt = 2025-04-08 11:00:00`
  - 5월 취소
    - `cancel-scenario-8`: `10000`, `canceledAt = 2025-05-12 16:00:00`
- 계산:
  - 총 판매 금액 = `0`
  - 환불 금액 = `10000`
  - 순 판매 금액 = `-10000`
  - 플랫폼 수수료 = `0`
  - 정산 예정 금액 = `-10000`
- 기대 결과:
  - `platformFeeAmount = 0`
  - `expectedPayoutAmount = -10000`
- 확인 포인트:
  - 환불만 있는 월에는 수수료가 음수가 되지 않고 0원으로 처리되는지 확인한다.

## 시나리오 8. 플랫폼 수수료는 양수, 정산 예정 금액은 음수
- 계정: `creator_scenario_8`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-05`
- 검증 순서:
  - `creator_scenario_8 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-05` 호출
- 원천 데이터:
  - 4월 판매
    - `sale-scenario-10`: `30000`, `20%`, `paidAt = 2025-04-05 09:00:00`
  - 5월 판매
    - `sale-scenario-11`: `10000`, `30%`, `paidAt = 2025-05-15 13:00:00`
  - 5월 취소
    - `cancel-scenario-9`: `30000`, 원본 판매 `sale-scenario-10`, `canceledAt = 2025-05-20 10:00:00`
- 계산:
  - 총 판매 금액 = `10000`
  - 환불 금액 = `30000`
  - 순 판매 금액 = `-20000`
  - 수수료율별 순액
    - `20%` 그룹: `-30000`
    - `30%` 그룹: `10000`
  - 플랫폼 수수료 = `0 + (10000 * 30%) = 3000`
  - 정산 예정 금액 = `-20000 - 3000 = -23000`
- 기대 결과:
  - `platformFeeAmount > 0`
  - `expectedPayoutAmount < 0`
- 확인 포인트:
  - 같은 월 신규 판매로 양수 수수료가 생기더라도, 환불이 더 크면 최종 정산 예정 금액은 음수가 될 수 있음을 확인한다.

## 시나리오 9. 판매만 있는 순수 정산
- 계정: `creator_scenario_9`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-06`
- 검증 순서:
  - `creator_scenario_9 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-06` 호출
- 원천 데이터:
  - `sale-scenario-12`: `20000`, `35%`, `paidAt = 2025-06-05 10:00:00`
  - `sale-scenario-13`: `30000`, `35%`, `paidAt = 2025-06-18 11:00:00`
- 계산:
  - 총 판매 금액 = `50000`
  - 환불 금액 = `0`
  - 순 판매 금액 = `50000`
  - 플랫폼 수수료 = `50000 * 35% = 17500`
  - 정산 예정 금액 = `50000 - 17500 = 32500`
- 기대 결과:
  - `grossSalesAmount = 50000`
  - `refundAmount = 0`
  - `netSalesAmount = 50000`
  - `platformFeeAmount = 17500`
  - `expectedPayoutAmount = 32500`

## 시나리오 10. 순 판매 금액이 정확히 0원
- 계정: `creator_scenario_10`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-06`
- 검증 순서:
  - `creator_scenario_10 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-06` 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-14`: `10000`, `35%`, `paidAt = 2025-06-08 10:30:00`
  - 취소
    - `cancel-scenario-10`: `10000`, `canceledAt = 2025-06-09 09:00:00`
- 계산:
  - 총 판매 금액 = `10000`
  - 환불 금액 = `10000`
  - 순 판매 금액 = `0`
  - 플랫폼 수수료 = `0`
  - 정산 예정 금액 = `0`
- 기대 결과:
  - `grossSalesAmount = 10000`
  - `refundAmount = 10000`
  - `netSalesAmount = 0`
  - `platformFeeAmount = 0`
  - `expectedPayoutAmount = 0`

## 시나리오 11. 수수료 소수점 올림
- 계정: `creator_scenario_11`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-06`
- 검증 순서:
  - `creator_scenario_11 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-06` 호출
- 원천 데이터:
  - `sale-scenario-15`: `10001`, `35%`, `paidAt = 2025-06-12 14:00:00`
- 계산:
  - 총 판매 금액 = `10001`
  - 환불 금액 = `0`
  - 순 판매 금액 = `10001`
  - 플랫폼 수수료 = `10001 * 35% = 3500.35`
  - 올림 적용 후 플랫폼 수수료 = `3501`
  - 정산 예정 금액 = `10001 - 3501 = 6500`
- 기대 결과:
  - `platformFeeAmount = 3501`
  - `expectedPayoutAmount = 6500`

## 시나리오 12. 여러 수수료율 판매와 환불이 함께 있는 복합 정산
- 계정: `creator_scenario_12`
- 호출: `GET /creator/settlements/monthly?yearMonth=2025-07`
- 검증 순서:
  - `creator_scenario_12 / qwe123123!`로 로그인
  - `GET /creator/settlements/monthly?yearMonth=2025-07` 호출
- 원천 데이터:
  - 판매
    - `sale-scenario-16`: `30000`, `35%`, `paidAt = 2025-06-05 09:00:00`
    - `sale-scenario-17`: `20000`, `35%`, `paidAt = 2025-06-18 15:00:00`
    - `sale-scenario-18`: `40000`, `40%`, `paidAt = 2025-07-12 11:00:00`
    - `sale-scenario-19`: `30000`, `45%`, `paidAt = 2025-07-25 16:00:00`
  - 취소
    - `cancel-scenario-11`: `10000`, 원본 판매 `sale-scenario-16`, `canceledAt = 2025-07-13 10:00:00`
    - `cancel-scenario-12`: `5000`, 원본 판매 `sale-scenario-18`, `canceledAt = 2025-07-28 09:00:00`
- 7월 계산에 반영되는 판매:
  - `sale-scenario-18`, `sale-scenario-19`
- 7월 계산에 반영되는 취소:
  - `cancel-scenario-11`, `cancel-scenario-12`
- 계산:
  - 총 판매 금액 = `40000 + 30000 = 70000`
  - 환불 금액 = `10000 + 5000 = 15000`
  - 순 판매 금액 = `70000 - 15000 = 55000`
  - 수수료율별 순액
    - `35%` 그룹: `-10000`
    - `40%` 그룹: `40000 - 5000 = 35000`
    - `45%` 그룹: `30000`
  - 플랫폼 수수료 = `0 + (35000 * 40%) + (30000 * 45%) = 0 + 14000 + 13500 = 27500`
  - 정산 예정 금액 = `55000 - 27500 = 27500`
- 기대 결과:
  - `grossSalesAmount = 70000`
  - `refundAmount = 15000`
  - `netSalesAmount = 55000`
  - `platformFeeAmount = 27500`
  - `expectedPayoutAmount = 27500`

## 운영자 확인 포인트
- 계정: `admin-1`
- 호출:
  - 예상 정산 집계: `GET /admin/settlements?from=2025-04-01&to=2025-07-31`
  - 실제 정산 상태 조회: `GET /admin/settlement-management?from=2025-01&to=2025-03`
- 검증 순서:
  - `admin-1 / qwe123123!`로 로그인
  - 먼저 `GET /admin/settlements?from=2025-04-01&to=2025-07-31` 호출
  - 이후 `GET /admin/settlement-management?from=2025-01&to=2025-03` 호출
- 확인 포인트:
  - 예상 정산 집계에서는 위 시나리오들의 월별 계산 결과가 크리에이터별로 반영된다.
  - 실제 정산 상태 조회에서는 `creator_scenario_5`의 `PENDING`, `CONFIRMED`, `PAID` 정산 3건을 확인할 수 있다.
