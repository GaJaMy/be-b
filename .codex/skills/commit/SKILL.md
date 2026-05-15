---
name: commit
description: Write and execute git commits that follow repository commit conventions from the current worktree changes. Use when the user asks to commit current work, create a commit message, summarize changes into a conventional commit, or wants Codex to choose a type such as feat, fix, docs, refactor, test, or chore and run git commit. Also use it when the user wants the development log updated as part of the commit flow.
---

# Commit

## Overview

현재 git 변경사항을 검토하고 가장 적절한 커밋 타입을 고른 뒤, 한국어 헤더와 짧은 본문을 작성해 non-interactive git 명령으로 커밋한다.

## Workflow

### 1. Inspect current changes

- Check `git status --short` first.
- Review the changed files and diff summary before writing the message.
- Understand the dominant purpose of the change instead of mirroring every small edit.
- Do not include unrelated user changes unless the user clearly wants everything committed together.

### 2. Choose the commit type

Use the type that best represents the main outcome.

- `feat`: new feature or new user-facing capability
- `fix`: bug fix or behavior correction
- `docs`: documentation-only changes
- `refactor`: structural improvement without behavior change
- `test`: test addition or test-only update
- `chore`: maintenance, config, tooling, cleanup

When multiple types are possible, choose the type that matches the main reason the user would describe the work.

### 3. Write the commit message

Use this repository style:

```text
<type> : <summary>
```

Write the body as short bullets that describe the main work. Do not make it overly detailed.

```text
<type> : <summary>

- ...를 구현했다
- ...를 추가했다
- ...를 수정했다
```

Rules:

- Keep the header to the main outcome only.
- Write the header and body in Korean by default.
- Prefer one language consistently within a single commit message.
- The body should usually be 1 to 3 bullets.
- Describe what was developed or changed, not every file name.

### 4. Execute the commit

- Stage only the files that belong to the intended commit.
- Use non-interactive git commands.
- Prefer:

```bash
git add <files>
git commit -m "<type> : <summary>" -m "- first change\n- second change"
```

- If the worktree contains unrelated changes, avoid staging them.
- Do not amend existing commits unless the user explicitly asks for it.

### 5. Update the development log

- Before committing, update `docs/development-log.md` if the current work introduced implementation or documentation changes worth recording.
- Follow the existing log structure:
  - `## YYYY-MM-DD`
  - `### 작업 단위`
  - short bullets describing what was developed or changed
- Add only the current work.
- Do not duplicate an existing log entry for the same work.
- Include the development log update in the same commit when it reflects the committed changes.

## Output expectations

When the user asks for a commit, do the work instead of only suggesting a message unless blocked.

Before committing:

- identify the changed scope
- determine the best commit type
- draft a short Korean header
- draft a brief Korean body
- update `docs/development-log.md` when needed

After committing:

- report the final commit header
- summarize the committed scope briefly
- mention whether the development log was updated

## Examples

### Example 1

User request:

```text
현재 작업 내용 커밋해줘
```

Possible result:

```text
docs : 프로젝트 컨텍스트 및 API 문서를 추가했다

- 프로젝트 개요, 도메인 설계, ERD 문서를 추가했다
- 개발 규칙과 API 명세 문서를 정리했다
```

### Example 2

User request:

```text
로그인 기능 작업한 내용 커밋해줘
```

Possible result:

```text
feat : 크리에이터와 운영자 로그인 흐름을 추가했다

- 크리에이터와 운영자 로그인 유스케이스를 구현했다
- 인증 API와 보안 처리 구성을 추가했다
```
