# Racknote API

Racknote is a backend API for managing **Eurorack modular synthesizer cases** and their physical structure (cases, rows, width (HP), height (U) ), with the goal of later supporting module placements and patch metadata.

This project is being developed as a personal project to accommodate my own needs, as an artist that plays with modular synthesisers, with an emphasis on:

- clean domain modeling
- explicit data invariants
- database-first design with migrations
- production-oriented project structure

It is intentionally focused on backend engineering rather than UI.

---

## Domain overview

In Eurorack systems:

- A **case** has a fixed width (measured in HP)
- A case is composed of **rows**
- All rows in a case share the same width
- Each row has a vertical height (measured in U)
- Modules are placed inside rows and must respect physical constraints

These constraints are modeled explicitly in the database and domain layer, rather than being left to frontend logic.

---

## Current features

- PostgreSQL schema managed via **Flyway**
- Domain entities:
  - `Case` (name, width)
  - `CaseRow` (row index, height)
- Enforced invariants:
  - a case cannot exist without rows
  - row widths are derived from the case (single source of truth)
- Read-only endpoint exposing the case layout for frontend rendering

### API example

GET /api/cases/main


Returns the full layout of the main Eurorack case (case metadata + ordered rows), ready to be consumed by a frontend grid renderer.

---

## Tech stack

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway**
- **Docker** (local database)
- Feature-first package organization

---

## Project structure

The codebase is organized by **feature**, with layers inside each feature:

cases/
api/ // controllers and DTOs
domain/ // JPA entities
repo/ // repositories


This mirrors common structure in larger production codebases and keeps domain boundaries explicit.

---

## Running locally

### Requirements
- Java 21
- Docker

### Start the database
```bash
docker compose up -d
Run the application
./scripts/env.sh ./mvnw spring-boot:run

Current Status:
This project is actively evolving.

## Planned next steps include:

- module placements within rows

- validation rules (no overlap, no overflow)

- read-optimized projections for rack rendering

- authentication once write operations are introduced

Notes:
This API is backend-focused by design.

A separate frontend (React / Next.js) will consume it for visual rack layout and interaction (emulation of a modular synth patch) to log.