# 개발 규칙

## 목적
이 문서는 이 프로젝트에서 일관된 구조로 기능을 구현하기 위한 개발 규칙을 정리한다.  
기본 방향은 실무 적합형 클린 아키텍처이며, 계층 책임과 데이터 전달 규칙을 명확히 유지하는 데 목적이 있다.

## 아키텍처 원칙
- 의존 방향은 `Controller -> UseCase -> Service -> Repository`로 고정한다.
- 상위 계층은 하위 계층을 의존할 수 있지만, 반대 방향 의존은 허용하지 않는다.
- API 버전 관리는 현재 범위에서 고려하지 않는다.

## 계층별 역할
### Controller
- HTTP 요청을 받는다.
- 요청 값을 `Request` 객체로 전달한다.
- 직접 비즈니스 로직을 처리하지 않는다.
- 반드시 `UseCase`를 호출해 응답을 생성한다.

### UseCase
- 하나의 기능 단위를 나타낸다.
- 여러 `Service`를 조합해 하나의 유스케이스를 완성한다.
- `Controller`로부터 `Request`를 받고 최종 `Response`를 반환한다.
- `Service`에 데이터를 전달할 때는 `Command`로 변환해서 전달한다.
- `Request`를 `Service`로 직접 넘기지 않는다.

### Service
- 도메인 로직 또는 개별 작업 단위를 수행한다.
- 필요한 경우 `Repository`를 통해 데이터를 조회하거나 저장한다.
- 처리 결과는 `Result` 형태로 `UseCase`에 반환한다.
- 다른 계층의 HTTP 요청/응답 모델을 알지 않아야 한다.
- 순환 참조 방지를 위해 다른 `Service`를 의존하지 않는다.

### Repository
- 영속성 처리만 담당한다.
- 비즈니스 로직을 포함하지 않는다.

## 객체 전달 규칙
- `Controller -> UseCase`: `Request`
- `UseCase -> Service`: `Command`
- `Service -> UseCase`: `Result`
- `UseCase -> Controller`: `Response`

즉 `Request`, `Command`, `Result`, `Response`의 역할을 혼용하지 않는다.

## 트랜잭션 규칙
- 트랜잭션 경계는 기본적으로 `UseCase`에 둔다.
- 여러 `Service`를 묶어 하나의 기능을 처리하는 책임은 `UseCase`에 있으므로, 트랜잭션도 이 계층에서 시작하는 것을 원칙으로 한다.
- `Service`는 자신의 작업 의도가 드러나도록 트랜잭션 속성을 명시한다.
- 조회 전용 서비스는 `@Transactional(readOnly = true)`를 명시한다.
- 쓰기 작업 서비스도 필요한 경우 트랜잭션 의도를 애너테이션으로 분명히 표현한다.

## 구현 시 주의사항
- `Controller`가 `Service`를 직접 호출하지 않는다.
- `UseCase`가 `Repository`를 직접 호출하지 않는다.
- `Service`가 다른 `Service`를 직접 호출하지 않는다.
- `Service`는 `Request`, `Response`에 의존하지 않는다.
- 하나의 `UseCase`는 하나의 기능을 명확하게 설명할 수 있어야 한다.
- 기능이 복잡해져도 계층 역할을 섞지 않는다.
