# 개발 로그

## 작성 규칙

- 이 문서에는 날짜별 개발 내역만 기록한다.
- 각 날짜는 `## YYYY-MM-DD` 형식으로 작성한다.
- 날짜 아래에는 작업 단위를 `### 기능명` 형식으로 작성한다.
- 각 기능 섹션에는 실제로 개발한 내용만 bullet로 남긴다.
- 구현 의도, 회고, TODO, 잡담은 이 문서에 쓰지 않는다.

## 2026-05-12

### 프로젝트 컨텍스트 문서화

- `docs/context/project-overview.md` 문서를 추가하고 과제 원문 기준 필수 요구사항과 추가 요구사항을 구분해 정리했다.
- 크리에이터 정산 API의 배경 시나리오, 필수 구현 범위, 정산 규칙, 샘플 데이터 검증 기준을 문서화했다.

### 도메인 설계 초안 정리

- `docs/context/domain-design.md` 문서를 추가하고 `Creator`, `Admin`, `Course`, `SaleRecord`, `CancellationRecord` 기준의 도메인 구조를 정리했다.
- 로그인 요구를 반영해 크리에이터와 운영자 계정 모델, 인증/인가 방향, 정산 서비스 책임을 문서화했다.

### ERD 문서 작성

- `docs/context/erd.md` 문서를 추가하고 저장 대상 테이블을 표 형식으로 정리했다.
- `creator`, `admin`, `course`, `sale_record`, `cancellation_record`의 컬럼, 관계, 제약조건을 명시했다.

### 개발 규칙 문서 작성

- `docs/context/development-rules.md` 문서를 추가하고 실무 적합형 클린 아키텍처 기준의 계층 의존 규칙을 정리했다.
- `Controller -> UseCase -> Service -> Repository` 구조, `Request/Command/Result/Response` 전달 규칙, 트랜잭션 위치 원칙을 문서화했다.
- 순환 참조 방지를 위해 `Service`가 다른 `Service`를 의존하지 않는 규칙을 추가했다.

### API 명세 문서 작성

- `docs/context/api-spec.md` 문서를 추가하고 인증, 판매, 취소, 정산 API의 요청/응답 구조를 정리했다.
- 공통 응답 형식과 프로젝트 기준 에러 코드 체계를 문서화했다.

### 프로젝트 기본 설정 구성

- Swagger, JPA, MySQL, JWT, Validation, Flyway 의존성을 `build.gradle`에 추가했다.
- `SecurityConfig`, `SwaggerConfig`, `application.yaml`을 정리해 보안, 문서화, 데이터베이스 연결의 기본 구성을 잡았다.

### 커밋 스킬 정리

- 프로젝트 전용 Codex 커밋 스킬을 로컬 `.codex/skills/commit` 경로로 정리했다.
- 스킬 이름을 `commit`으로 변경하고 저장소 커밋 규칙에 맞는 헤더와 본문 작성 흐름을 정리했다.
- 커밋 수행 시 `docs/development-log.md`를 함께 갱신하도록 스킬 규칙을 추가했다.

### 공통 응답 및 예외 처리 추가

- `ApiResponse` 공통 응답 객체를 정리하고 성공/실패 응답 생성 메서드를 추가했다.
- `ErrorCode`, `BusinessException`, `GlobalExceptionHandler`를 연결해 공통 예외 응답 처리 기반을 구성했다.
- 입력 검증, 요청 파라미터, JSON 파싱, 비즈니스 예외를 일관된 응답 형식으로 반환하도록 정리했다.

### JWT 인증 기반 추가

- `JwtProvider`, `JwtProperties`, `JwtAuthenticationFilter`를 추가해 액세스 토큰 생성 및 검증 기반을 구성했다.
- 크리에이터와 운영자 인증 주체를 구분하는 `AuthenticatedPrincipal`, 전용 `UserDetailsService`, 인증 프로바이더 구조를 추가했다.
- 보안 예외 응답 처리를 위해 `SecurityResponseWriter`와 인증 관련 에러 코드를 정리하고 설정 값을 `application.yaml`에 반영했다.

### Flyway 초기 스키마 추가

- `src/main/resources/db/migration/V1__init_schema.sql` 파일을 추가했다.
- `creator`, `admin`, `course`, `sale_record`, `cancellation_record` 테이블과 기본 인덱스, 제약조건을 초기 마이그레이션으로 구성했다.

### API 명세 보정

- 판매 등록 요청에서 `saleId`, 취소 등록 요청에서 `cancelId`를 제거했다.
- 등록 식별자는 클라이언트 입력이 아니라 서버 생성 값으로 정리했다.

### 크리에이터/운영자 로그인 구현

- `api/auth`, `service/auth`, `repository` 구조를 추가해 크리에이터와 운영자 로그인 흐름을 구현했다.
- 로그인 요청/응답 DTO, 인증 컨트롤러, 유스케이스, 서비스, 저장소를 연결했다.
- 보안 설정과 JWT 인증 구조를 로그인 API 기준으로 보완했다.

### 초기 계정 시드 추가

- `Seeder`를 추가해 크리에이터와 운영자 초기 계정을 적재하는 기반을 구성했다.
- 인증 테스트와 로그인 흐름 확인에 필요한 기본 계정 데이터를 애플리케이션 실행 시점에 준비하도록 정리했다.

### 권한 정책 반영

- `SecurityConfig`에서 판매 등록과 취소 등록은 현재 단계에서 인증 없이 허용하도록 설정했다.
- 판매 내역 조회는 `ROLE_CREATOR`, 운영자 정산 조회는 `ROLE_ADMIN` 기준으로 접근 제어를 분리했다.

### 개발 규칙 단순화

- `Command`, `Result`를 강제하는 규칙을 제거하고 과도한 보일러플레이트를 줄이는 방향으로 문서를 수정했다.
- `Service`에는 웹 DTO를 직접 넘기지 않고, 필요한 값 또는 최소한의 서비스 전용 DTO만 사용하도록 정리했다.

### 판매/취소 API 구현 시작

- `api/sales`, `service`, `repository`, `domain` 구조를 추가해 판매 등록, 취소 등록, 판매 조회 기능의 기본 흐름을 구성했다.
- `SaleRecord`, `Course`, `CancellationRecord` 엔티티와 관련 저장소를 연결했다.
- 판매/취소 권한 정책과 API 문서, 개발 규칙을 현재 구현 방향에 맞게 보정했다.

### 커밋 스킬 한국어 기준 보정

- 커밋 스킬이 기본적으로 한글 헤더와 본문을 작성하도록 규칙과 예시를 수정했다.

### 에러 코드 보정

- 크리에이터 조회 실패 상황에 사용할 `NOT_FOUND_CREATOR` 에러 코드를 추가했다.
- API 명세의 인증 에러 코드 설명도 현재 코드 기준에 맞게 정리했다.

### 판매 조회 및 취소 등록 보강

- 판매 조회 API에 기간 조건과 인증 주체 기반 조회 흐름을 연결했다.
- 판매 취소 등록 API에 `saleId` path variable 기반 취소 생성 흐름을 연결했다.
- 판매/취소 DTO 이름과 서비스, 저장소 조회 로직을 현재 구현 방향에 맞게 정리했다.

## 2026-05-13

### 샘플 시드 데이터 추가

- `V2__insert_seed_data.sql`에 샘플 `course`, `sale_record` 데이터를 추가했다.
- 샘플 판매 데이터는 문서에 정의한 검증 시나리오 기준의 결제 일시와 금액을 반영했다.

### 초기 스키마 제약 보정

- 엔티티의 `ConstraintMode.NO_CONSTRAINT` 설정과 맞추기 위해 `V1__init_schema.sql`의 외래키 제약을 제거했다.
- 컬럼 구조와 조회용 인덱스는 유지하고 스키마 제약만 완화했다.

### 정산 조회 계산 로직 정리

- 정산 조회의 기간 경계 생성을 `SettlementUseCase`에서 일관되게 처리하도록 정리했다.
- 판매/취소 합계 계산을 내부 공통 메서드와 `SettlementMetrics` record로 묶어 중복을 줄였다.
- 정산 조회 서비스는 `LocalDateTime` 범위를 직접 받아 조회 책임만 가지도록 정리했다.

### 날짜 유틸 공통화

- `common/util/DateTimeUtil`을 추가해 월 시작, 월 종료 exclusive, 일 시작, 일 종료 exclusive 계산을 공통 메서드로 분리했다.
- `SettlementUseCase`의 날짜 경계 계산 로직을 유틸 메서드 호출로 정리했다.

### API 에러 코드 정리

- Bean Validation과 바인딩으로 처리할 수 있는 입력 오류 코드를 API 명세에서 제거했다.
- 입력 오류는 `COMMON_001`, 비즈니스/인증 오류는 개별 에러 코드로 유지하도록 문서를 정리했다.

### 정산 조회 API 구현

- `SettlementController`, `SettlementUseCase`, 정산 응답 DTO를 추가해 크리에이터 월별 정산 조회와 운영자 정산 집계 조회 흐름을 구현했다.
- 내부 집계용 `SettlementMetrics`와 날짜 유틸을 활용해 정산 계산 로직과 기간 경계 처리를 공통화했다.

### 인증/인가 에러 응답 정리

- `CustomAuthenticationEntryPoint`, `CustomAccessDeniedHandler`를 추가해 인증 실패와 권한 부족 상황을 공통 응답 형식으로 반환하도록 정리했다.
- `SecurityConfig`에 예외 처리 핸들러를 연결하고 인증 필요, 권한 없음 에러 코드를 보완했다.
- JWT 필터에서 토큰 예외 발생 시 `SecurityContext`를 비우고 인증 에러 응답만 남기도록 정리했다.

### 존재하지 않는 엔드포인트 404 처리 정리

- `NoHandlerFoundException`을 공통 예외 처리기에 연결해 존재하지 않는 경로 요청을 공통 응답 형식으로 반환하도록 정리했다.
- `application.yaml`에 핸들러 미존재 예외 전환 설정을 추가하고 기본 정적 리소스 매핑은 비활성화했다.
- `SecurityConfig`에서 보호 대상을 실제 구현된 API 경로만 명시하고 나머지 요청은 `permitAll()`로 열어 404 응답이 보안 규칙보다 먼저 막히지 않도록 정리했다.

### 허용되지 않은 HTTP 메서드 405 처리 정리

- `HttpRequestMethodNotSupportedException`을 공통 예외 처리기에 연결해 잘못된 HTTP 메서드 요청을 공통 응답 형식으로 반환하도록 정리했다.
- `COMMON_004` 에러 코드를 추가해 405 응답도 다른 예외와 같은 포맷으로 처리하도록 맞췄다.

### 날짜 입력 검증 메시지 정리

- 판매 등록과 취소 등록 요청의 `OffsetDateTime` 필드에 JSON 날짜 형식을 명시하고 필수값 검증 메시지를 보강했다.
- 조회 API의 `LocalDate`, `YearMonth` 파라미터에 필수값 메시지를 추가하고 날짜 형식 변환 실패 시 입력 형식을 안내하는 예외 처리를 보강했다.

### 무료 강의 금액 검증 기준 보정

- 판매 금액과 환불 금액은 무료 강의와 무료 환불 케이스를 허용하도록 `0 이상` 검증으로 조정했다.
- 금액 검증 메시지도 제약값과 일치하도록 함께 수정했다.

### KST 기준 일시 저장 로직 보정

- 판매 등록과 취소 등록 시 `OffsetDateTime` 입력값을 바로 `LocalDateTime`으로 버리지 않고 `Asia/Seoul` 기준으로 정규화한 뒤 저장하도록 정리했다.
- `DateTimeUtil`에 KST 변환 메서드를 추가해 정산 기준 시간대 정책을 코드에서 일관되게 유지하도록 맞췄다.

### 컨텍스트 문서 규칙 보정

- 프로젝트 개요 문서에 KST 기준 입력 일시를 내부 `LocalDateTime`으로 정규화해 저장한다는 원칙을 명시했다.
- API 명세 문서에 남아 있던 `Service`의 `Command/Result` 구현 기준 문구를 제거하고 현재 개발 규칙과 맞게 정리했다.

### 정산 상태 관리 설계 반영

- `domain-design` 문서에 `Settlement` 엔티티와 `SettlementStatus(PENDING, CONFIRMED, PAID)` 기준의 상태 관리 흐름을 추가했다.
- `erd` 문서에 `settlement` 테이블과 정산 상태, 금액 스냅샷, 생성/확정/지급 시각, 유니크 제약과 상태 전이 규칙을 반영했다.

### 정산 예상 조회와 실제 정산 관리 역할 분리

- 크리에이터 월별 정산 조회는 `Settlement` 조회가 아니라 원천 판매/취소 데이터를 기준으로 계산하는 예상 정산 조회라는 점을 문서에 명확히 반영했다.
- `Settlement`는 크리에이터 정산 생성 이후 관리자 확인, 확정, 지급 상태를 관리하는 실제 정산 데이터라는 점을 설계 문서와 ERD에 맞게 정정했다.

### 도메인 설계 문서 구조 정리

- `domain-design` 문서에서 `SettlementPolicy`, `SettlementService`, `UseCase` 같은 구현 계층 중심 설명을 제거했다.
- 엔티티, 상태, 관계, 정산 규칙, 정산 생성과 상태 전이 흐름 중심으로 문서를 다시 정리했다.

### 중복 정산 방지와 수수료율 이력 설계 보강

- `domain-design`에 동일 `creatorId + yearMonth` 조합 중복 정산 방지 규칙과 정산 생성 시점 유효 수수료율 적용 규칙을 추가했다.
- `erd`에 `settlement`의 수수료율 스냅샷 컬럼과 `fee_policy_history` 테이블을 추가하고, 과거 정산이 당시 수수료율을 유지하도록 설계를 보강했다.

### 월별 단일 수수료율 정책 명시

- 과제 원문의 `순 판매 금액 = 총 판매 - 환불`, `플랫폼 수수료 = 순 판매 금액의 수수료율%` 공식을 유지하기 위해 수수료율은 각 정산 대상 월마다 하나만 존재하는 것으로 해석했다.
- `FeePolicyHistory`를 기간 중간 변경 이력이 아니라 월별 수수료율 이력 구조로 정리하고, 관련 설계 문서와 ERD 표현을 이에 맞게 수정했다.

### 정산 관리 API 명세 추가

- `api-spec`에 크리에이터 정산 생성, 생성된 정산 목록/상세 조회, 운영자 정산 조회/확정/지급 처리 API를 추가했다.
- 수수료율 이력 등록/목록/상세 조회 API와 중복 정산, 월 종료 전 생성 금지, 상태 전이 충돌 관련 에러 코드를 함께 정리했다.
