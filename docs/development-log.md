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
