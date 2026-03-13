# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

See @README.md for the overall project overview.

## Architecture

Squishy Spaces is a monorepo with three directories:

- `backend/` — Fastify + TypeScript backend (currently the active codebase)
- `mobile/` — future Android app
- `docs/` — documentation

IMPORTANT: The backend is currently the primary active codebase.  
Unless explicitly instructed otherwise, focus development work in `backend/`.

The backend integrates with the OpenAI API to analyze room images and return structured interior design feedback.

## Backend Commands

All commands run from `backend/`.

```bash
npm install      # install dependencies
npm run dev      # start development server with tsx
npm run build    # compile TypeScript to dist/
npm start        # run compiled server
```

## Environment

Backend environment variables live in `backend/.env`.

Required variables:

- `OPENAI_API_KEY`
- `PORT` (defaults to 3000)

Never commit `.env` files.  
Keep `backend/.env.example` updated whenever environment variables change.

## Workflow

Before implementing non-trivial features:

1. Scan the repository
2. Propose a step-by-step implementation plan
3. List the files that will need to change
4. Only then implement the approved plan

Keep code changes minimal and scoped to the requested feature.

Avoid refactoring unrelated files unless explicitly requested.

When adding backend features:

- Prefer creating route handlers in `backend/src/routes/`
- Register routes from `backend/src/index.ts`
- Keep route handlers small and readable

After modifying backend code, ensure the TypeScript build succeeds.

## Code Style

- Use TypeScript with ES module syntax (`import` / `export`)
- Prefer small, readable functions over complex abstractions
- Keep route logic straightforward
- Avoid introducing new dependencies unless clearly necessary

## Important

When editing API routes:

- Do not modify existing routes unless required for the task
- Keep endpoints stable unless explicitly asked to change them
- Prefer minimal changes rather than large refactors