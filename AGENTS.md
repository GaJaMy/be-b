# Repository Guidelines

## Project Structure & Module Organization
This repository is a single-module Gradle project named `be-b`. Application code lives under `src/main/java/com/liveclass/be_b`, with shared configuration in `common/config`, security-related code in `security`, and domain logic under `domain`. Runtime configuration is in `src/main/resources/application.yaml`; place static files in `src/main/resources/static` and server-rendered templates in `src/main/resources/templates`. Tests mirror the main package layout under `src/test/java`.

## Build, Test, and Development Commands
Use the Gradle wrapper so the team stays on the same toolchain.

- `./gradlew bootRun`: start the Spring Boot app locally.
- `./gradlew build`: compile, run tests, and assemble the artifact.
- `./gradlew test`: run the JUnit test suite only.
- `./gradlew clean`: remove previous build outputs.

The project targets Java 21, so use a JDK 21 installation when running commands locally.

## Coding Style & Naming Conventions
Follow standard Spring Boot and Java conventions: 4-space indentation, one public class per file, `PascalCase` for classes, `camelCase` for methods and fields, and lowercase package names. Keep configuration classes under `common/config`. Prefer descriptive service and controller names such as `AuthController` or `UserService`. Lombok is enabled; use it selectively to reduce boilerplate without hiding core behavior.

## Testing Guidelines
Testing uses JUnit 5 through `spring-boot-starter-test`, with Spring context tests enabled via `@SpringBootTest`. Name test classes with the `*Test` or `*Tests` suffix and mirror the production package structure. Add focused unit or slice tests for new business logic, security rules, and persistence behavior. Run `./gradlew test` before opening a pull request.

## Commit & Pull Request Guidelines
Recent history uses short, typed commit messages such as `chore : mac 관련 ignore 설정`. Keep that pattern: `<type> : <summary>` with concise scope, for example `feat : add JWT auth filter`. For pull requests, include a short description, testing notes, linked issue if applicable, and sample request/response details for API changes.

## Security & Configuration Tips
`application.yaml` expects external secrets and database settings via environment variables: `MYSQL_DB_URL`, `MYSQL_DB_USER_NAME`, `MYSQL_DB_PASSWORD`, and `JWT_SECRET_KEY`. Do not commit real credentials. Because JPA uses `ddl-auto: validate` and Flyway is included, prefer schema changes through migrations rather than ad hoc database edits.

## Codex Usage Logging
Maintain [docs/codex-usage-log.md](/Users/howard/Desktop/personal/be-b/docs/codex-usage-log.md) as a cumulative record of what the user uses Codex for. Add a new entry whenever the user gives a new type of instruction, and do not add duplicates for instruction types already recorded.
