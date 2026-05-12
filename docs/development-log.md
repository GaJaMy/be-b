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
