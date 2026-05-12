---
name: commit
description: Write and execute git commits that follow repository commit conventions from the current worktree changes. Use when the user asks to commit current work, create a commit message, summarize changes into a conventional commit, or wants Codex to choose a type such as feat, fix, docs, refactor, test, or chore and run git commit. Also use it when the user wants the development log updated as part of the commit flow.
---

# Commit

## Overview

Review the current git changes, choose the most appropriate commit type, write a concise header and short body, then commit with a non-interactive git command.

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

- implemented ...
- added ...
- updated ...
```

Rules:

- Keep the header to the main outcome only.
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
- draft a short header
- draft a brief body
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
docs : add project context and API documentation

- added project overview, domain design, and ERD documents
- added development rules and API specification
```

### Example 2

User request:

```text
로그인 기능 작업한 내용 커밋해줘
```

Possible result:

```text
feat : add creator and admin login flow

- implemented creator and admin login use cases
- added authentication API and security handling
```
