# QTD — Questions To Discuss

> **FHNW Internet Technology — Group Project**
> A web application that helps people start meaningful conversations through guided, themed question sessions for friends, dates, deep talks, and fun gatherings.

---

## Group Composition

| Name | Main Contribution |
|------|-------------------|
| **Danila Anfilofyev** | Backend development, JPA domain model, service layer, security configuration, deployment |
| **Platon Pashkevych** | REST API design, controllers, OpenAPI documentation, frontend–backend integration, |
| **Snizhana Pashkevych** | Frontend (Thymeleaf templates), UX design, responsive layout, CSS design system, testing|

---

## Links

| Resource | URL |
|----------|-----|
| 🎥 **Video Presentation** | *(add YouTube / SWITCHtube / Microsoft Stream link before submission)* |
| 📄 **OpenAPI / Swagger Documentation** | `https://<codespace-url>/swagger-ui.html` (or `http://localhost:8080/swagger-ui.html` locally) |
| 💻 **GitHub Repository** | https://github.com/PlatonUser/qtd-internet-technology.git |

---

## Table of Contents

- [1. Analysis](#1-analysis)
  - [1.1 Scenario & Domain](#11-scenario--domain)
  - [1.2 Actors](#12-actors)
  - [1.3 Use Cases](#13-use-cases)
  - [1.4 User Stories](#14-user-stories)
- [2. Domain Design](#2-domain-design)
  - [2.1 Domain Model](#21-domain-model)
  - [2.2 Database Schema](#22-database-schema)
  - [2.3 Seed Data](#23-seed-data)
- [3. Frontend Implementation](#3-frontend-implementation)
  - [3.1 Technology Choice](#31-technology-choice)
  - [3.2 Design System](#32-design-system)
  - [3.3 Views](#33-views)
- [4. Business Logic & API Design](#4-business-logic--api-design)
  - [4.1 Business Rules](#41-business-rules)
  - [4.2 REST API](#42-rest-api)
- [5. Data & API Implementation](#5-data--api-implementation)
  - [5.1 Architecture](#51-architecture)
  - [5.2 Backend Technology Stack](#52-backend-technology-stack)
  - [5.3 Package Structure](#53-package-structure)
  - [5.4 Design Patterns & Principles](#54-design-patterns--principles)
- [6. Security](#6-security)
- [7. Demonstrator — Installation & Running](#7-demonstrator--installation--running)
  - [7.1 Running Locally](#71-running-locally)
  - [7.2 Running on GitHub Codespaces](#72-running-on-github-codespaces)
- [8. Project Management](#8-project-management)
  - [8.1 Milestones](#81-milestones)
  - [8.2 Requirements Coverage](#82-requirements-coverage)

---

# 1. Analysis

## 1.1 Scenario & Domain

**QTD (Questions To Discuss)** is a web application that helps people start meaningful conversations through guided, themed question sessions. In many social situations — meeting friends, going on a date, or spending time with a group — people often struggle to break the ice or move beyond small talk. QTD addresses this by providing themed categories of curated questions that guide users through a structured, turn-based conversation.

A typical session looks like this:

1. A user opens the application and sees a responsive grid of category cards (*Friends*, *Dating*, *Deep Talk*, *Fun Topics*).
2. The user picks a category, enters the names of all players, and starts a session.
3. The system randomly selects **5 active questions** from the chosen category.
4. Questions are displayed one at a time. Each player writes their own short answer per question (or skips).
5. At the end, a summary view shows every question alongside every player's answer — a recap of the conversation.

In parallel, an **administrator** can log in to a separate admin panel to manage the content: create, edit and deactivate categories and questions, and review past sessions and usage statistics.

The system is built as a responsive web application that works seamlessly on both desktop and mobile devices.

## 1.2 Actors

| Actor | Role |
|-------|------|
| **Public User** | Browses categories, starts sessions, plays through questions, sees the summary. No login required. |
| **Administrator** | Logs in via the admin panel to manage categories, questions and sessions. |

## 1.3 Use Cases

### Public User
- **Browse Categories** — Open the homepage and see all active categories with icon, name and description.
- **Set Up a Session** — Pick a category, enter one or more player names, confirm.
- **Play a Session** — Receive 5 random questions one by one; every named player writes their answer per question, then advances.
- **View Summary** — On the last question the session is marked complete and a summary page shows every question with every player's answer.

### Administrator
- **Log In** — Authenticate via a form login at `/admin/login` (Spring Security).
- **Dashboard** — See counts of categories, questions, sessions and answers, plus a per-category breakdown.
- **Manage Categories (CRUD)** — Create, list, edit, deactivate or delete categories. Deletion cascades to the category's questions (`orphanRemoval = true`).
- **Manage Questions (CRUD)** — Create, list, edit (text, category, active flag) or delete questions; validation enforces minimum length of 10 characters.
- **Review Sessions** — List all past sessions ordered newest first with date, category, question count and answer count.

## 1.4 User Stories

The seven generic user stories from the assessment are mapped 1-to-1 onto the implemented features:

| Generic Story (assessment §2.1) | Implemented As |
|----------|----------------|
| 1. Admin — Web app on mobile and desktop | Responsive layout via fluid grid + media queries; works on phone, tablet and desktop |
| 2. Admin — Consistent visual appearance | Shared CSS design system (`/css/app.css`) + Thymeleaf layout fragments (`fragments/layout.html`) used across every admin page |
| 3. Admin — List views for business data | `/admin/categories`, `/admin/questions`, `/admin/sessions`, `/admin/dashboard` |
| 4. Admin — Edit and create views | `/admin/categories/new`, `/admin/categories/{id}/edit`, `/admin/questions/new`, `/admin/questions/{id}/edit` |
| 5. Admin — Log-in to authenticate | `/admin/login` (Spring Security form login + BCrypt-hashed admin user) |
| 6. User — List views for public pages | Home page (`/`) shows category list; session play (`/session/{id}/play`) and summary (`/session/{id}/summary`) are public list/detail views |
| 7. *(Optional)* User — Authenticate to access confidential data | Implemented for the admin actor; public users intentionally remain anonymous for low-friction session start |

### Extended User Stories (project-specific)

| # | User Story |
|---|------------|
| US-A1 | As an admin I want to **log in** so I can securely manage the content. |
| US-A2 | As an admin I want a **dashboard with key counts** so I can see usage at a glance. |
| US-A3 | As an admin I want to **CRUD categories** with an active/inactive toggle so I can curate topics. |
| US-A4 | As an admin I want to **CRUD questions** with category assignment so I can grow the question bank. |
| US-A5 | As an admin I want to **review past sessions** so I can monitor how the app is used. |
| US-U1 | As a user I want to **browse categories** so I can pick a topic. |
| US-U2 | As a user I want to **add several players by name** so the session is personalised. |
| US-U3 | As a user I want to **answer questions one by one** so the conversation flows naturally. |
| US-U4 | As a user I want to **see a final summary** so we can reflect on everyone's answers. |
| US-U5 | As a user I want the experience to **work on my phone** so we can play anywhere. |

---

# 2. Domain Design

## 2.1 Domain Model

The QTD domain model is built around **five JPA entities** that capture the lifecycle of a question session — from content management (categories & questions) to play-time (session, players, answers) and administrator identity.

```
┌─────────────┐       ┌──────────────┐ 1     * ┌──────────────────┐
│   AppUser   │       │   Category   ├─────────►│     Question     │
│─────────────│       │──────────────│         │──────────────────│
│ id          │       │ id           │         │ id               │
│ username    │       │ name         │         │ text  (min 10)   │
│ password    │       │ slug  (uniq) │         │ category (FK)    │
│ role        │       │ description  │         │ active           │
└─────────────┘       │ icon  (emoji)│         └──────────────────┘
                      │ color        │                  ▲
                      │ active       │                  │
                      └──────┬───────┘                  │
                             │ 1                        │
                             │                          │ *
                             ▼ *                        │
                      ┌──────────────┐ 1            * ┌──────────────────┐
                      │   Session    │────────────────►│  SessionAnswer   │
                      │──────────────│                 │──────────────────│
                      │ id           │                 │ id               │
                      │ category(FK) │                 │ session (FK)     │
                      │ startedAt    │                 │ question (FK)    │
                      │ completed    │                 │ playerName       │
                      │ players []   │ (ElementColl.)  │ answerText (1000)│
                      │ questionIds[]│ (ElementColl.)  └──────────────────┘
                      └──────────────┘
```

**Relationships**

| Relation | Cardinality | Cascade |
|----------|-------------|---------|
| `Category` → `Question` | 1 — \* | `CascadeType.ALL`, `orphanRemoval = true` |
| `Category` → `Session` | 1 — \* | — (FK only) |
| `Session` → `SessionAnswer` | 1 — \* | `CascadeType.ALL`, `orphanRemoval = true` |
| `Question` → `SessionAnswer` | 1 — \* | — (FK only) |
| `Session` → `players` (List<String>) | 1 — \* | `@ElementCollection` → `session_players` |
| `Session` → `questionIds` (List<Long>) | 1 — \* | `@ElementCollection` → `session_question_ids` |

> The session keeps its picked `questionIds` and `players` denormalised so that re-runs of the player flow are reproducible even if an admin later edits questions or categories.

## 2.2 Database Schema

The application uses an **H2 in-memory relational database** (`jdbc:h2:mem:qtddb`), recreated on every startup (`ddl-auto=create-drop`) and seeded via `src/main/resources/data.sql`.

| Table | Purpose |
|-------|---------|
| `app_user` | Administrator records (extensible; demo uses an in-memory admin from `SecurityConfig`) |
| `category` | Discussion topic categories |
| `question` | Individual questions linked to a category |
| `session` | A play-through within a category |
| `session_players` | List of player names per session (collection table) |
| `session_question_ids` | Ordered list of selected question ids per session (collection table) |
| `session_answer` | One answer per player per question per session |

### Detailed Table Structure

**`category`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| name | VARCHAR | NOT NULL, `@NotBlank` |
| slug | VARCHAR | NOT NULL, UNIQUE, `@NotBlank` |
| description | VARCHAR | nullable |
| icon | VARCHAR | NOT NULL, default `💬` |
| color | VARCHAR | default `general` |
| active | BOOLEAN | NOT NULL, default `true` |

**`question`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| text | VARCHAR(500) | NOT NULL, `@NotBlank`, min 10 chars |
| category_id | BIGINT | FK → `category.id`, NOT NULL |
| active | BOOLEAN | NOT NULL, default `true` |

**`session`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| category_id | BIGINT | FK → `category.id`, NOT NULL |
| started_at | TIMESTAMP | NOT NULL, default `now()` |
| completed | BOOLEAN | default `false` |

**`session_answer`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| session_id | BIGINT | FK → `session.id`, NOT NULL |
| question_id | BIGINT | FK → `question.id`, NOT NULL |
| player_name | VARCHAR | nullable |
| answer_text | VARCHAR(1000) | nullable |

**`app_user`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| username | VARCHAR | NOT NULL, UNIQUE, `@NotBlank` |
| password | VARCHAR | NOT NULL, `@NotBlank` |
| role | VARCHAR | NOT NULL, default `ADMIN` |

## 2.3 Seed Data

`data.sql` populates the database on every startup with:

- **4 categories**: Friends 👥, Dating ❤️, Deep Talk 🧠, Fun Topics 😊
- **28 questions** (7 per category — well above the `MIN_QUESTIONS_TO_START = 3` business threshold)
- **2 sample completed sessions** with answers from 4 different players, so the *Sessions* admin view and the dashboard are non-empty on first launch

---

# 3. Frontend Implementation

## 3.1 Technology Choice

The frontend is implemented as a **full-code, server-rendered web application** using **Thymeleaf 3** templates served by the same Spring Boot process as the REST API.

> **Justification for choosing full-code over a low-code tool (assessment §2.3):**
> We initially prototyped screens in Budibase, but several application needs could not be expressed cleanly there: a multi-step session flow with a per-question form holding *N* dynamic inputs (one per player), a layout shared between public and admin pages via fragments, custom client-free CSS theming with CSS variables, and tight integration with Spring Security's form-login + CSRF mechanism. Implementing these in Thymeleaf gave us full control over UX details and a single deployable artefact. This choice was discussed and confirmed with the lecturer. The documentation of everything related to the work with budibase is provided to the lecturer in addition.

The decision keeps the application as a **single deployable unit** (one Spring Boot JAR / one Codespace port), which makes the demonstrator easier to instantiate and reproducible.

## 3.2 Design System

A single CSS file (`src/main/resources/static/css/app.css`) defines a consistent visual identity:

- **Dark UI theme** with CSS custom properties (`--bg`, `--surface`, `--primary`, `--ok`, `--warn`, `--danger`, `--radius`, `--shadow`, …) — single source of truth for colours and radii
- **Typography**: Google Fonts *Inter* (400/500/600/700/800)
- **Layout primitives**: `.page`, `.hero`, `.categories-grid`, `.btn`, `.card`, `.form-row`, `.table`
- **Responsive**: fluid grid for category cards, sticky nav, breakpoints for mobile
- **Reusable Thymeleaf fragments** (`fragments/layout.html`) → `head(title)`, `navbar`, `adminNavbar` — used by every page to guarantee consistency

## 3.3 Views

The application implements **11 distinct views** — far above the *minimum of 4* required by the assessment.

### Public Views

| Route | Template | Description |
|-------|----------|-------------|
| `GET /` | `home.html` | Hero + responsive grid of active category cards with "Start Session" CTA |
| `GET /session/setup?categoryId={id}` | `session/setup.html` | Form to add player names before starting |
| `POST /session/start` | *(redirect)* | Creates the session and redirects to `play?q=1` |
| `GET /session/{id}/play?q={n}` | `session/play.html` | One question at a time, one input per player, progress bar |
| `POST /session/{id}/play` | *(redirect)* | Stores all player answers, advances to next question or summary |
| `GET /session/{id}/summary` | `session/summary.html` | Final recap: every question × every player's answer |

### Admin Views (require login)

| Route | Template | Description |
|-------|----------|-------------|
| `GET /admin/login` | `admin/login.html` | Login form (`username`, `password`) — Spring Security form login |
| `GET /admin/dashboard` | `admin/dashboard.html` | KPI cards (categories, questions, sessions, answers) + per-category breakdown |
| `GET /admin/categories` | `admin/categories.html` | Categories list table with active-question counts |
| `GET /admin/categories/new`, `…/{id}/edit` | `admin/category-form.html` | Shared create / edit form |
| `GET /admin/questions` | `admin/questions.html` | Questions list with category & active status |
| `GET /admin/questions/new`, `…/{id}/edit` | `admin/question-form.html` | Shared create / edit form |
| `GET /admin/sessions` | `admin/sessions.html` | All sessions ordered by date desc, with question & answer counts |

---

# 4. Business Logic & API Design

## 4.1 Business Rules

The service layer enforces two business rules that correspond to real-world enterprise constraints (assessment §2.2: *at least one business rule*).

### Rule 1 — Minimum active questions required to start a session

A session can only be created if the chosen category has **at least 3 active questions** (`MIN_QUESTIONS_TO_START = 3`).

```java
// SessionService.createSession()
List<Question> active = questionRepository.findByCategoryIdAndActive(categoryId, true);
if (active.size() < MIN_QUESTIONS_TO_START) return null;
```

If violated, the controller redirects back to `/session/setup?categoryId={id}` with a user-facing flash error:
*"Could not start session. The category needs at least 3 active questions."*

**Justification:** prevents trivially short sessions and guarantees a meaningful experience.

### Rule 2 — Fixed-size, randomised question selection

Every new session selects **exactly 5 randomised active questions** (`QUESTIONS_PER_SESSION = 5`) from the category and freezes them into `session.questionIds`. Even if the same category is replayed, the question set varies.

```java
// SessionService.createSession()
Collections.shuffle(active);
List<Long> questionIds = active.stream()
        .limit(QUESTIONS_PER_SESSION)
        .map(Question::getId)
        .collect(Collectors.toList());
```

**Justification:** guarantees a consistent session length while keeping replays fresh; freezing the IDs makes the session reproducible even if an admin edits questions later.

## 4.2 REST API

All REST endpoints are documented via **OpenAPI 3.0 / Swagger UI** with `@Tag`, `@Operation` and `@SecurityRequirement` annotations on the controllers.

- **Swagger UI:** `/swagger-ui.html`
- **OpenAPI JSON:** `/api-docs`
- **Base URL:** `http://localhost:8080`

### Categories — `/api/categories` (Basic Auth required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | List all categories |
| `GET` | `/api/categories/{id}` | Get a category by id |
| `POST` | `/api/categories` | Create a category |
| `PUT` | `/api/categories/{id}` | Update a category |
| `DELETE` | `/api/categories/{id}` | Delete a category (cascades to questions) |

### Questions — `/api/questions` (Basic Auth required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/questions?categoryId={id}&activeOnly={bool}` | List questions, optionally filtered by category and active flag |
| `GET` | `/api/questions/{id}` | Get a question by id |
| `POST` | `/api/questions` | Create — body `{ "text", "categoryId" }` |
| `PUT` | `/api/questions/{id}` | Update — body `{ "text"?, "categoryId"?, "active"? }` |
| `DELETE` | `/api/questions/{id}` | Delete a question |

### Sessions — `/api/sessions` (Basic Auth required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/sessions` | List all sessions (newest first) |
| `GET` | `/api/sessions/{id}` | Get a session by id |
| `POST` | `/api/sessions` | Start a session — body `{ "categoryId", "players": [..] }` |
| `PUT` | `/api/sessions/{id}/complete` | Mark a session as completed |
| `DELETE` | `/api/sessions/{id}` | Delete a session and its answers |
| `GET` | `/api/sessions/{id}/questions?limit=5` | Get the questions selected for a session |
| `POST` | `/api/sessions/{id}/answers` | Add an answer — body `{ "questionId", "playerName", "answerText" }` |
| `GET` | `/api/sessions/{id}/answers` | List all answers for a session |

### Response codes

| Code | Meaning |
|------|---------|
| `200` | Success |
| `400` | Bean-validation error (e.g. question text under 10 chars) |
| `401` | Missing / invalid Basic Auth credentials |
| `403` | Authenticated but not an admin |
| `404` | Resource not found |
| `500` | Unexpected server error |

> The REST API is intentionally **admin-only**. Public users interact with the application through the server-rendered Thymeleaf views (`/`, `/session/**`), which call the service layer directly — no public REST surface is exposed. This keeps the data API tight and authenticated by default.

---

# 5. Data & API Implementation

## 5.1 Architecture

The application reflects a **three-layer architecture deployed across two logical tiers** (assessment §2.2: *three layers on at least two tiers*).

```
┌────────────────────────────────────────────────────────────┐
│  CLIENT TIER — Browser (any device, responsive)            │
│  • Renders HTML/CSS from Thymeleaf                         │
│  • Submits forms (HTML POST), receives redirects           │
└────────────────────────┬───────────────────────────────────┘
                         │ HTTPS / HTTP
                         │
┌────────────────────────▼───────────────────────────────────┐
│  SERVER TIER — Spring Boot 3.2                             │
│                                                            │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  PRESENTATION LAYER                                  │  │
│  │  • Thymeleaf MVC controllers (web)                   │  │
│  │    HomeController, SessionViewController,            │  │
│  │    AdminViewController, AdminCategoriesController,   │  │
│  │    AdminQuestionsController, AdminSessionsController │  │
│  │  • REST controllers (JSON API, Basic Auth)           │  │
│  │    CategoryController, QuestionController,           │  │
│  │    SessionController                                 │  │
│  └────────────────────────┬─────────────────────────────┘  │
│                           │                                │
│  ┌────────────────────────▼─────────────────────────────┐  │
│  │  BUSINESS LOGIC LAYER (Service)                      │  │
│  │  CategoryService, QuestionService, SessionService,   │  │
│  │  AdminDashboardService                               │  │
│  │  → Business rules, transactional boundaries          │  │
│  └────────────────────────┬─────────────────────────────┘  │
│                           │                                │
│  ┌────────────────────────▼─────────────────────────────┐  │
│  │  DATA ACCESS LAYER (Spring Data JPA Repositories)    │  │
│  │  CategoryRepository, QuestionRepository,             │  │
│  │  SessionRepository, SessionAnswerRepository          │  │
│  └────────────────────────┬─────────────────────────────┘  │
└────────────────────────────┼───────────────────────────────┘
                             │ JDBC
                             ▼
                  ┌──────────────────────┐
                  │  H2 in-memory RDBMS  │
                  └──────────────────────┘
```

**Why two tiers from one process?** The client (browser) and server (Spring Boot) are distinct tiers communicating over HTTP. Within the server tier, the three layers (Presentation → Service → Repository) are strictly separated: controllers never touch repositories directly, repositories never know about HTTP, and the service layer is the only place where business rules live.

## 5.2 Backend Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Web (MVC) | (Boot-managed) | Both REST and Thymeleaf controllers |
| Spring Data JPA | (Boot-managed) | ORM + repository abstraction |
| Spring Security | (Boot-managed) | Form login + HTTP Basic Auth + BCrypt |
| Spring Validation | (Boot-managed) | Bean validation (`@NotBlank`, `@Size`) |
| Thymeleaf | (Boot-managed) | Server-side HTML templating |
| H2 Database | (Boot-managed) | In-memory relational DB for the demonstrator |
| SpringDoc OpenAPI | 2.3.0 | OpenAPI 3 / Swagger UI |
| Lombok | (Boot-managed) | `@Getter`/`@Setter`/`@Builder`/`@NoArgsConstructor`/`@AllArgsConstructor` |
| Maven Wrapper | 3.6.3 | Build (no Maven install required) |

## 5.3 Package Structure

```
ch.fhnw.qtd
├── QtdApplication.java             # @SpringBootApplication entry point
│
├── config/
│   ├── SecurityConfig.java         # Two security chains (API + Web) + BCrypt user store + CORS
│   └── OpenApiConfig.java          # @OpenAPIDefinition + @SecurityScheme(basicAuth)
│
├── model/                          # JPA entities (Lombok @Builder)
│   ├── AppUser.java
│   ├── Category.java
│   ├── Question.java
│   ├── Session.java
│   └── SessionAnswer.java
│
├── repository/                     # Spring Data JPA repositories
│   ├── CategoryRepository.java
│   ├── QuestionRepository.java     #   + countByCategoryIdAndActive, findByCategoryIdAndActive
│   ├── SessionRepository.java      #   + findAllByOrderByStartedAtDesc, countByCategoryId
│   └── SessionAnswerRepository.java#   + countBySessionId, deleteBySessionIdAndQuestionId
│
├── service/                        # Business logic
│   ├── CategoryService.java
│   ├── QuestionService.java
│   ├── SessionService.java         # Business rules live here (min questions, random pick)
│   └── AdminDashboardService.java
│
└── controller/
    ├── HomeController.java                 # GET /  (public home)
    ├── SessionViewController.java          # /session/**  (public play flow, Thymeleaf)
    ├── AdminViewController.java            # /admin/login, /admin/dashboard
    ├── AdminCategoriesController.java      # /admin/categories  (full CRUD + redirects + flash)
    ├── AdminQuestionsController.java       # /admin/questions   (full CRUD)
    ├── AdminSessionsController.java        # /admin/sessions    (read-only list)
    ├── CategoryController.java             # /api/categories    (REST)
    ├── QuestionController.java             # /api/questions     (REST)
    └── SessionController.java              # /api/sessions      (REST)

src/main/resources/
├── application.properties          # H2, JPA, OpenAPI, CORS config
├── data.sql                        # Seed data (run after schema creation)
├── static/css/app.css              # Design system (CSS variables, components)
└── templates/                      # Thymeleaf views
    ├── home.html
    ├── fragments/layout.html       # Shared head + navbars (DRY)
    ├── admin/                      # 7 admin views
    │   ├── login.html, dashboard.html
    │   ├── categories.html, category-form.html
    │   ├── questions.html, question-form.html
    │   └── sessions.html
    └── session/                    # 3 player views
        ├── setup.html, play.html, summary.html
```

## 5.4 Design Patterns & Principles

| Principle / Pattern | Where applied |
|---------------------|---------------|
| **Layered architecture** | Strict Controller → Service → Repository separation; controllers never call repositories directly |
| **MVC pattern** | Thymeleaf controllers return view names + populate `Model`; data flows are explicit |
| **Repository pattern** | All persistence goes through Spring Data JPA interfaces |
| **DTO-light approach** | Entities are exposed via REST; `@JsonIgnore` hides back-references to avoid recursion (e.g. `Category.questions`, `Session.answers`) |
| **Builder pattern** | Lombok `@Builder` used on every entity for safe, readable construction |
| **DRY** | Shared Thymeleaf `fragments/layout.html` and `category-form.html` / `question-form.html` reused for both create and edit |
| **CRUD paradigm** | Every domain entity exposes Create, Read, Update, Delete — via REST *and* via the Thymeleaf admin UI |
| **PRG (Post/Redirect/Get)** | All form submissions in the admin UI redirect after POST with `RedirectAttributes` flash messages — prevents double-submit |
| **Single Responsibility** | `AdminDashboardService` is split out from `CategoryService`/`SessionService` because aggregating dashboard stats is a distinct concern |
| **Transactional boundary** | `SessionService.saveAnswersForQuestion` is `@Transactional` — delete-then-insert is atomic |

---

# 6. Security

Security is configured in `ch.fhnw.qtd.config.SecurityConfig` using **two distinct `SecurityFilterChain` beans**, evaluated in order.

### Chain 1 — REST API (`@Order(1)`, matches `/api/**`)

- **HTTP Basic Authentication** — every request to `/api/**` requires `Authorization: Basic <base64(user:pass)>` with role `ADMIN` (assessment §2.5 milestone 6).
- **CSRF disabled** — appropriate for a stateless JSON API.
- **CORS enabled** — `*` origin pattern, all headers and methods (suitable for the demonstrator; tighten before production).

### Chain 2 — Web UI (`@Order(2)`, default)

- **Public paths:** `/`, `/session/**`, `/css/**`, `/js/**`, `/images/**`, `/favicon.ico`, `/webjars/**`, `/h2-console/**`, `/swagger-ui/**`, `/swagger-ui.html`, `/api-docs/**`, `/admin/login`
- **Admin paths:** `/admin/**` requires role `ADMIN`
- **Form login** at `/admin/login` → on success redirects to `/admin/dashboard`; on failure to `/admin/login?error=true`
- **Logout** via POST to `/admin/logout` → redirects to `/`
- **CSRF enabled** for browser forms (disabled only for the H2 console)
- **Headers**: `frame-options` disabled so the H2 console iframe renders

### Credentials

The admin user is provisioned in-memory at startup with a **BCrypt-hashed password**:

| Field | Value |
|-------|-------|
| Username | `admin` |
| Password | `admin` |
| Role | `ADMIN` |

> Demo credentials only. Replace with a persistent `app_user`-backed `UserDetailsService` before any non-demo deployment.

### Summary table

| Surface | Auth scheme | Required role |
|---------|-------------|---------------|
| `/api/**` | HTTP Basic | `ADMIN` |
| `/admin/**` | Form login (cookie session) | `ADMIN` |
| `/` and `/session/**` | None | — |
| `/swagger-ui.html`, `/api-docs/**` | None (browse), Basic Auth used inside Swagger to call APIs | — |
| `/h2-console` | None | — |

---

# 7. Demonstrator — Installation & Running

## 7.1 Running Locally

**Prerequisites**
- Java 17+
- Maven (or use the included `mvnw` wrapper — nothing to install)

**Steps**

```bash
# 1. Clone the repository
git clone <repository-url>
cd qtd-internet-technology/backend

# 2. Start the application (Linux/macOS)
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The application starts on **`http://localhost:8080`**. Schema is created on startup and seeded automatically from `data.sql`.

### Useful URLs

| URL | What it is |
|-----|------------|
| `http://localhost:8080/` | Public home page (category cards) |
| `http://localhost:8080/admin/login` | Admin login (user: `admin`, password: `admin`) |
| `http://localhost:8080/admin/dashboard` | Admin dashboard with KPIs |
| `http://localhost:8080/swagger-ui.html` | OpenAPI / Swagger UI |
| `http://localhost:8080/api-docs` | OpenAPI 3.0 JSON spec |
| `http://localhost:8080/h2-console` | H2 web console |

### H2 console connection settings

- **JDBC URL:** `jdbc:h2:mem:qtddb`
- **User Name:** `sa`
- **Password:** *(leave blank)*

### Calling the REST API

```bash
# Public web is open — but /api is admin-only:
curl -u admin:admin http://localhost:8080/api/categories
curl -u admin:admin http://localhost:8080/api/questions?categoryId=1&activeOnly=true
curl -u admin:admin -X POST http://localhost:8080/api/sessions \
     -H "Content-Type: application/json" \
     -d '{"categoryId":1,"players":["Alice","Bob"]}'
```

## 7.2 Running on GitHub Codespaces

The repository contains a `.devcontainer/devcontainer.json` pre-configured with Java 17, Maven 3.6.3 and Node 18, and forwards port `8080`.

1. On the GitHub repository page: **`Code` → `Codespaces` → `Create codespace on main`**.
2. Wait for the container image to build (≈ 1 min on first run).
3. In the terminal:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
4. When port **`8080`** is forwarded, open the **Ports** tab → right-click port `8080` → **Port visibility → Public** (required so the URL is reachable from outside Codespaces).
5. Click **Open in Browser** on port `8080`. The full app — home, session flow, admin login, Swagger UI — is available at:
   ```
   https://<codespace-name>-8080.app.github.dev/
   https://<codespace-name>-8080.app.github.dev/swagger-ui.html
   ```

`server.forward-headers-strategy=framework` is already set so that Spring honours the Codespaces reverse-proxy headers and form-login redirects work correctly under the public Codespace URL.

---

# 8. Project Management

### Roles & Cooperation

| Area | Responsible |
|------|-------------|
| Backend / Data Model / Security | Danila Anfilofyev |
| REST API / Documentation / Integration | Platon Pashkevych |
| Frontend / UX / Design System | Snizhana Pashkevych |

The team coordinated through weekly syncs and used the GitHub repository as the single source of truth — all source code, the seed dataset, the dev-container configuration and the documentation (this README) live there. Commits and pull-requests provide a full timeline of the work.

## 8.1 Milestones

| # | Milestone (assessment §2.5) | Outcome | Status |
|---|-----------------------------|---------|--------|
| 1 | **Analysis** — Scenario, use case, user stories | §1 of this README — domain & actors defined, 10 user stories | ✅ |
| 2 | **Domain Design** — Domain model | §2 — 5 JPA entities + 2 collection tables, ERD diagram | ✅ |
| 3 | **Frontend implementation** — Design, prototyping, realisation | §3 — 11 Thymeleaf views, shared layout fragments, full CSS design system | ✅ |
| 4 | **Business Logic & API design** — Rules + REST API | §4 — 2 enforced rules; full REST surface documented with OpenAPI annotations | ✅ |
| 5 | **Data & API implementation** — JPA, services, controllers | §5 — 4 repositories, 4 services, 9 controllers (3 REST + 6 web) | ✅ |
| 6 | **Security** — API-level authentication | §6 — HTTP Basic on `/api/**` + form login on `/admin/**`, BCrypt hashing | ✅ |
| 7 | **Demonstrator** — End-to-end integration | §7 — single Spring Boot artefact, runs locally and on Codespaces, seed data pre-loaded | ✅ |

## 8.2 Requirements Coverage

| Strict requirement (assessment §2.2 / §2.3) | How it is met |
|---------------------------------------------|---------------|
| ≥ 3 layers across ≥ 2 tiers | Browser (tier 1) ⇄ Spring Boot (tier 2) with Presentation / Service / Repository layers |
| ≥ 4 views for the user stories | 11 views (4 public + 7 admin) |
| ≥ 4 entities in the schema | 5 entities (`AppUser`, `Category`, `Question`, `Session`, `SessionAnswer`) + 2 collection tables |
| ≥ 1 business rule in the service layer | 2 rules (minimum active questions; fixed random 5-question pick) |
| Responsive, consistent visual design | Shared CSS design system, fluid grid, sticky nav, mobile-first breakpoints |
| OOP, design patterns, DRY, CRUD | Layered architecture, Repository, Builder, MVC, PRG, fragment reuse, full CRUD per entity |
| Enterprise-grade backend (≥ Spring Boot 3.0 / Java 17) | Spring Boot 3.2.0, Java 17 |
| Relational database | H2 in-memory (justified for reproducibility of the demonstrator) |
| GitHub version control | Repository linked above; full commit history |
| OpenAPI 3.0 documentation | SpringDoc OpenAPI 2.3.0 at `/swagger-ui.html` and `/api-docs` |
| README on GitHub | This file — covers analysis, design, implementation, installation, links, group, video, OpenAPI, milestones |
| Presentation video | Linked in the *Links* section above |
| GitHub Codespaces deployment | `.devcontainer/devcontainer.json` ready; instructions in §7.2 |

---

*FHNW University of Applied Sciences and Arts Northwestern Switzerland — School of Business — Internet Technology module, 2026.*
