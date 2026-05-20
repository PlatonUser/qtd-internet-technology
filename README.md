# QTD – Questions To Discuss

> **FHNW Internet Technology Group Project**
> A web application that helps people start meaningful conversations through guided, themed question sessions.

---

## Group Composition

| Name | Contribution |
|------|-------------|
| *(Member 1 – Danila Anfilofyev)* | Backend development, data model, services, security, deployment |
| *(Member 2 – Platon Pashkevych)* | Frontend (Budibase UI), UX design, testing |
| *(Member 3 – Snizhana Pashkevych)* | API design, controllers, documentation, integration |

---

## Links

| Resource | URL |
|----------|-----|
| 🎥 Video Presentation | *(add link – YouTube / SWITCHtube / Microsoft Stream)* |
| 🌐 Deployed Web Application | *(add Budibase app link)* |
| 📄 OpenAPI / Swagger Documentation | `https://<codespace-url>/swagger-ui.html` |
| 💻 GitHub Repository | *(add GitHub repo link)* |

---

## Contents

- [Analysis](#analysis)
  - [Scenario](#scenario)
  - [Actors](#actors)
  - [Use Cases](#use-cases)
  - [User Stories](#user-stories)
- [Domain Design](#domain-design)
  - [Domain Model](#domain-model)
  - [Database Schema](#database-schema)
- [Business Logic](#business-logic)
- [API Design](#api-design)
- [Frontend Design](#frontend-design)
- [Implementation](#implementation)
  - [Architecture](#architecture)
  - [Backend Technology](#backend-technology)
  - [Frontend Technology](#frontend-technology)
  - [Security](#security)
- [Installation & Running](#installation--running)
  - [Running Locally](#running-locally)
  - [GitHub Codespaces](#github-codespaces)
- [Project Management](#project-management)
  - [Roles](#roles)
  - [Milestones](#milestones)

---

# Analysis

## Scenario

**QTD (Questions To Discuss)** is a web-based application designed to help users start meaningful conversations through guided question sessions. In many social situations — meeting friends, going on a date, or spending time with a group — people often struggle to initiate interesting or deeper discussions. QTD addresses this by providing themed categories of questions that guide users through structured conversation sessions.

Users open the application, browse available categories (such as *Friends*, *Dating*, *Deep Talk*, or *Fun Topics*), and start a session where questions are displayed one by one. During the session, players can optionally enter their name and short answers or reflections. At the end of the session, the application presents a summary showing all questions and the responses entered by each player.

The system is implemented as a responsive web application that works on both desktop and mobile devices. It integrates a Budibase frontend, a Spring Boot REST API backend, and an H2 relational database that stores categories, questions, sessions, and answers.

In addition to public users who participate in question sessions, the system includes an **administrator role**. Administrators can log in and manage content — creating, editing, and deleting categories and questions — to keep the application relevant and well-maintained.

---

## Actors

The system defines two main actors:

**Public User**
- Browses available question categories
- Starts a question session for a chosen category
- Enters player names before the session begins
- Answers questions during the session
- Views a summary of all questions and answers at the end

**Administrator**
- Logs into the system via Basic Authentication
- Creates, edits, and deletes categories
- Creates, edits, and deletes questions assigned to categories
- Views session data and usage statistics

---

## Use Cases

### Public User Use Cases

**Browse Categories**
The user opens the application and sees a responsive grid of available question categories, each showing an icon, name, and description.

**Start Session**
The user selects a category and is prompted to enter player names. A new session is created and a set of questions is randomly selected from the category.

**Answer Questions**
Questions are displayed one by one. Each player can enter a short answer or leave it blank and skip to the next question.

**View Session Summary**
After all questions are answered, the application displays a summary page showing each question alongside the answers provided by all players during the session.

---

### Administrator Use Cases

**Log In**
The administrator authenticates using HTTP Basic Auth credentials to gain access to the admin panel.

**Manage Categories (CRUD)**
The administrator can create new categories, view all categories in a list, update category details (name, description, icon, color, active status), and delete categories — subject to the business rule that prevents deletion while active questions exist.

**Manage Questions (CRUD)**
The administrator can create new questions linked to a category, view all questions with optional filtering by category, update question text or active status, and delete questions.

**View Sessions**
The administrator can view a list of all sessions, including when they started, which category was used, and whether the session was completed.

---

## User Stories

### Administrator User Stories

| # | User Story |
|---|-----------|
| US-A1 | As an **admin**, I want to **log in to the system** so that I can **securely manage the application**. |
| US-A2 | As an **admin**, I want to **use the application on different mobile devices and desktop computers** so that I can **manage it from anywhere**. |
| US-A3 | As an **admin**, I want to **see a consistent visual appearance** so that I can **navigate easily**. |
| US-A4 | As an **admin**, I want to **view categories in a list** so that I can **manage existing discussion topics**. |
| US-A5 | As an **admin**, I want to **create and edit categories** so that I can **organise questions into meaningful groups**. |
| US-A6 | As an **admin**, I want to **create and edit questions** so that I can **maintain the discussion content**. |
| US-A7 | As an **admin**, I want to **delete outdated categories or questions** so that **the application remains relevant**. |
| US-A8 | As an **admin**, I want to **view sessions** so that I can **monitor how the application is used**. |

### Public User Stories

| # | User Story |
|---|-----------|
| US-U1 | As a **user**, I want to **browse question categories** so that I can **choose a topic for a conversation**. |
| US-U2 | As a **user**, I want to **use list views** so that I can **access public pages**. |
| US-U3 | As a **user**, I want to **start a question session** so that I can **explore discussion topics with others**. |
| US-U4 | As a **user**, I want to **see questions one by one** so that **the conversation flows naturally**. |
| US-U5 | As a **user**, I want to **optionally write answers during a session** so that I can **reflect on the questions**. |
| US-U6 | As a **user**, I want to **see a summary of the session** so that I can **review all questions and answers at the end**. |

---

# Domain Design

## Domain Model

The QTD domain model is built around five core entities that capture the full lifecycle of a question session — from content management to user interaction and answer recording.

```
┌─────────────┐       ┌──────────────┐       ┌──────────────────┐
│   AppUser   │       │   Category   │       │     Question     │
│─────────────│       │──────────────│       │──────────────────│
│ id          │       │ id           │1     *│ id               │
│ username    │       │ name         ├───────►│ text             │
│ password    │       │ slug         │       │ category (FK)    │
│ role        │       │ description  │       │ active           │
└─────────────┘       │ icon         │       └──────────────────┘
                      │ color        │              │
                      │ active       │1             │ *
                      └──────────────┘       ┌──────────────────┐
                              │              │  SessionAnswer   │
                              │1             │──────────────────│
                              │              │ id               │
                              ▼*             │ session (FK)     │
                      ┌──────────────┐       │ question (FK)    │
                      │   Session    │1     *│ playerName       │
                      │──────────────├───────►│ answerText       │
                      │ id           │       └──────────────────┘
                      │ category(FK) │
                      │ startedAt    │
                      │ completed    │
                      └──────────────┘
```

**Relationships:**
- `Category → Question` (One-to-Many): A category contains multiple questions. Deletion cascades.
- `Category → Session` (One-to-Many): A session is created within a specific category.
- `Session → SessionAnswer` (One-to-Many): A session captures one answer per player per question.
- `Question → SessionAnswer` (One-to-Many): A question can be answered across multiple sessions.

---

## Database Schema

The application uses an **H2 in-memory relational database**, seeded with sample data on startup via `data.sql`.

### Table Overview

| Table | Purpose |
|-------|---------|
| `app_user` | Administrator credentials and roles |
| `category` | Discussion topic categories |
| `question` | Individual questions linked to a category |
| `session` | A user's conversation session within a category |
| `session_answer` | Answers recorded per player per question per session |

### Detailed Table Structure

**`app_user`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| username | VARCHAR | NOT NULL, UNIQUE |
| password | VARCHAR | NOT NULL |
| role | VARCHAR | NOT NULL, default `ADMIN` |

**`category`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| name | VARCHAR | NOT NULL |
| slug | VARCHAR | NOT NULL, UNIQUE |
| description | VARCHAR | nullable |
| icon | VARCHAR | NOT NULL, default `💬` |
| color | VARCHAR | default `general` |
| active | BOOLEAN | NOT NULL, default `true` |

**`question`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| text | VARCHAR(500) | NOT NULL, min 10 chars |
| category_id | BIGINT | FK → category.id |
| active | BOOLEAN | NOT NULL, default `true` |

**`session`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| category_id | BIGINT | FK → category.id |
| started_at | TIMESTAMP | NOT NULL |
| completed | BOOLEAN | default `false` |

**`session_answer`**

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, auto-generated |
| session_id | BIGINT | FK → session.id |
| question_id | BIGINT | FK → question.id |
| player_name | VARCHAR | nullable |
| answer_text | VARCHAR(1000) | nullable |

### Seed Data

The database is pre-populated (via `data.sql`) with:
- **4 Categories**: Friends, Dating, Deep Talk, Fun Topics
- **8 Questions**: 2 per category (expandable)
- **2 Sample Sessions** with player answers to demonstrate the summary feature

---

# Business Logic

The service layer enforces two key business rules that correspond to real enterprise-level constraints:

### Rule 1 – Minimum Active Questions Required to Start a Session

A session can only be started if the selected category has **at least 3 active questions**.

**Location:** `SessionService.getQuestionsForSession()` — fetches only active questions from the category. If fewer than 3 exist, the session cannot meaningfully proceed and the frontend blocks the start action.

**Justification:** Prevents sessions that would feel incomplete or trivially short, ensuring a quality experience.

### Rule 2 – Cannot Delete a Category That Has Active Questions

An administrator cannot delete a category while it still contains active questions.

**Location:** Enforced at the service and controller level via `CategoryService`. The frontend admin panel checks this condition and displays an appropriate warning before allowing the delete action.

**Justification:** Prevents accidental loss of content that is actively in use, mirroring a standard referential integrity business rule.

---

# API Design

The backend exposes a RESTful API. All endpoints follow REST conventions (CRUD via HTTP verbs, resource-based URLs, JSON request/response bodies).

**Base URL:** `http://localhost:8080`

**OpenAPI docs:** `http://localhost:8080/swagger-ui.html`

**H2 Console:** `http://localhost:8080/h2-console`

---

## Public Endpoints (no authentication required)

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | Get all categories |
| `GET` | `/api/categories/{id}` | Get a single category by ID |

### Questions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/questions` | Get all questions (optional `?categoryId=&activeOnly=true`) |
| `GET` | `/api/questions/{id}` | Get a single question by ID |

### Sessions

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/sessions` | Create a new session (`{ "categoryId": 1 }`) |
| `GET` | `/api/sessions/{id}` | Get session details |
| `GET` | `/api/sessions/{id}/questions?limit=5` | Get randomised questions for a session |
| `POST` | `/api/sessions/{id}/answers` | Submit an answer (`{ "questionId", "playerName", "answerText" }`) |
| `GET` | `/api/sessions/{id}/answers` | Get all answers for a session |
| `GET` | `/api/sessions/{id}/answer-count` | Get the count of answers submitted |
| `PUT` | `/api/sessions/{id}/complete` | Mark a session as completed |

---

## Admin Endpoints (Basic Auth required)

### Category Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | List all categories |
| `POST` | `/api/categories` | Create a category |
| `PUT` | `/api/categories/{id}` | Update a category |
| `DELETE` | `/api/categories/{id}` | Delete a category (if no active questions) |

### Question Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/questions` | List all questions |
| `POST` | `/api/questions` | Create a question (`{ "text", "categoryId" }`) |
| `PUT` | `/api/questions/{id}` | Update a question |
| `DELETE` | `/api/questions/{id}` | Delete a question |

### Session Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/sessions` | List all sessions (ordered by date desc) |
| `DELETE` | `/api/sessions/{id}` | Delete a session |

---

## Error Responses

| HTTP Code | Meaning |
|-----------|---------|
| `400` | Validation error (e.g. question text too short) |
| `401` | Unauthorized (admin endpoint without credentials) |
| `404` | Resource not found |
| `409` | Business rule conflict (e.g. category has active questions) |
| `422` | Business rule violation (e.g. not enough questions to start) |
| `500` | Internal server error |

---

# Frontend Design

The frontend is implemented using **Budibase** (low-code), integrated with the Spring Boot REST API.

The UI follows a consistent design system with a card-based layout, emoji icons, colour-coded categories, and responsive behaviour across mobile and desktop viewports.

## Views

### Public Views

| Route | View | Description |
|-------|------|-------------|
| `/` | **Home** | Responsive grid of category cards with icon, name, description, and "Start Session" button |
| `/session/setup` | **Player Setup** | Form to enter player names before a session begins |
| `/session/play` | **Session Play** | Question-by-question flow; each player enters their answer in sequence |
| `/session/play/final` | **Session Summary** | Final summary showing all questions and answers for all players |

### Admin Views

| Route | View | Description |
|-------|------|-------------|
| `/admin/login` | **Admin Login** | Secure login form using Basic Auth |
| `/admin/dashboard` | **Dashboard** | Overview stats and category breakdown |
| `/admin/categories` | **Category Management** | CRUD table for categories |
| `/admin/questions` | **Question Management** | CRUD table for questions, filterable by category |
| `/admin/sessions` | **Session List** | Read-only list of all sessions with metadata |

---

# Implementation

## Architecture

The application follows a **three-layer, two-tier architecture**:

```
┌──────────────────────────────────────────────────┐
│               FRONTEND TIER (Budibase)           │
│          Presentation / UI Layer                  │
│   Category cards, session flow, admin panels     │
└──────────────────────┬───────────────────────────┘
                       │ REST API (HTTP / JSON)
┌──────────────────────▼───────────────────────────┐
│              BACKEND TIER (Spring Boot)           │
│                                                   │
│  ┌─────────────────────────────────────────────┐ │
│  │   Controller Layer (REST API endpoints)     │ │
│  │   CategoryController, QuestionController,   │ │
│  │   SessionController, HomeController         │ │
│  └──────────────────────┬──────────────────────┘ │
│                         │                         │
│  ┌──────────────────────▼──────────────────────┐ │
│  │   Service Layer (Business Logic)            │ │
│  │   CategoryService, QuestionService,         │ │
│  │   SessionService                            │ │
│  └──────────────────────┬──────────────────────┘ │
│                         │                         │
│  ┌──────────────────────▼──────────────────────┐ │
│  │   Repository Layer (Data Access / JPA)      │ │
│  │   CategoryRepository, QuestionRepository,   │ │
│  │   SessionRepository, SessionAnswerRepository│ │
│  └──────────────────────┬──────────────────────┘ │
│                         │                         │
│  ┌──────────────────────▼──────────────────────┐ │
│  │   H2 In-Memory Relational Database          │ │
│  └─────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────┘
```

**Design Patterns Used:**
- **MVC / Layered Architecture** – strict separation of controllers, services, and repositories
- **Repository Pattern** – Spring Data JPA interfaces abstract all database operations
- **Builder Pattern** – Lombok `@Builder` used consistently across all entities
- **DRY Principle** – shared service logic avoids duplication between controller endpoints
- **CRUD Paradigm** – all entities expose full Create, Read, Update, Delete operations via REST

---

## Backend Technology

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Data JPA | (Boot-managed) | ORM and repository pattern |
| Spring Security | (Boot-managed) | Authentication and CORS |
| Spring Validation | (Boot-managed) | Bean validation (`@NotBlank`, `@Size`) |
| H2 Database | (Boot-managed) | In-memory relational database |
| SpringDoc OpenAPI | 2.3.0 | Swagger/OpenAPI 3.0 documentation |
| Lombok | (Boot-managed) | Boilerplate reduction (`@Getter`, `@Builder`, etc.) |
| Maven | 3.6.3 | Build and dependency management |

**Package structure:**
```
ch.fhnw.qtd
├── QtdApplication.java        # Application entry point
├── config/
│   └── SecurityConfig.java    # CORS and security filter chain
├── controller/
│   ├── CategoryController.java
│   ├── QuestionController.java
│   ├── SessionController.java
│   └── HomeController.java
├── model/
│   ├── AppUser.java
│   ├── Category.java
│   ├── Question.java
│   ├── Session.java
│   └── SessionAnswer.java
├── repository/
│   ├── CategoryRepository.java
│   ├── QuestionRepository.java
│   ├── SessionRepository.java
│   └── SessionAnswerRepository.java
└── service/
    ├── CategoryService.java
    ├── QuestionService.java
    └── SessionService.java
```

---

## Frontend Technology

| Technology | Purpose |
|------------|---------|
| **Budibase** | Low-code frontend application builder |
| REST API integration | All data is fetched dynamically from the Spring Boot backend |
| Responsive layout | Works on mobile and desktop without modifications |
| Component-based design | Reusable UI components for cards, forms, and tables |

The frontend was first prototyped in Budibase to establish screen layouts, colour system, and navigation flow before implementing the final version.

---

## Security

The application implements **HTTP Basic Authentication** for admin endpoints via Spring Security:

- **Public endpoints** (`/api/categories`, `/api/questions`, `/api/sessions/**`) → no authentication required
- **Admin CRUD operations** → require valid admin credentials via `Authorization: Basic <base64>` header
- **CORS** → configured to allow requests from Budibase (`https://inttech.budibase.app`) and all origins (`*`) for development
- **H2 Console** and **Swagger UI** → accessible without authentication for development/demo purposes
- **CSRF** → disabled (stateless REST API with no session cookies)

Admin credentials are set in `application.properties` / Spring Security's in-memory user store.

> **Default admin credentials (for demo):** `admin` / `admin` *(change before production use)*

---

# Installation & Running

## Prerequisites

- Java 17+
- Maven 3.6+

## Running Locally

```bash
# Clone the repository
git clone <repository-url>
cd qtd-internet-technology

# Start the backend
cd backend
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`.

| URL | Description |
|-----|-------------|
| `http://localhost:8080/` | API welcome message |
| `http://localhost:8080/api/categories` | Categories endpoint |
| `http://localhost:8080/swagger-ui.html` | OpenAPI / Swagger UI |
| `http://localhost:8080/h2-console` | H2 Database console |

**H2 Console connection settings:**
- JDBC URL: `jdbc:h2:mem:qtddb`
- Username: `sa`
- Password: *(leave blank)*

---

## GitHub Codespaces

The project includes a `.devcontainer/devcontainer.json` pre-configured for GitHub Codespaces with Java 17, Maven, and Node 18.

1. Open the repository on GitHub and click **"Code" → "Codespaces" → "Create codespace"**
2. Wait for the container to build and dependencies to install
3. In the terminal, run:
   ```bash
   cd backend && ./mvnw spring-boot:run
   ```
4. When port `8080` becomes available, click **"Open in Browser"** or copy the Codespace URL
5. **Make port 8080 public** in the Ports tab so the Budibase frontend can reach the API
6. The Swagger UI will be available at: `https://<codespace-name>-8080.app.github.dev/swagger-ui.html`

---

# Project Management

## Roles

| Role | Responsibilities |
|------|-----------------|
| **Backend / Data** | Domain model design, JPA entities, repositories, service layer, H2 seed data |
| **Frontend / UX** | Budibase application design and implementation, UI/UX consistency, responsive layout |
| **API / DevOps** | REST controllers, Spring Security, OpenAPI documentation, GitHub Codespaces configuration |
| **Documentation** | README, analysis, use cases, user stories, domain design, milestone tracking |

---

## Milestones

| # | Milestone | Description | Status |
|---|-----------|-------------|--------|
| 1 | **Analysis** | Scenario ideation, use case analysis, user story writing | ✅ Done |
| 2 | **Domain Design** | Domain model definition, database schema, entity relationships | ✅ Done |
| 3 | **Frontend Implementation** | Budibase UI design, prototyping, and realization of all views | ✅ Done |
| 4 | **Business Logic & API Design** | Definition of business rules, REST API design, endpoint specification | ✅ Done |
| 5 | **Data & API Implementation** | Implementation of JPA repositories, services, and REST controllers | ✅ Done |
| 6 | **Security** | Spring Security configuration, Basic Auth for admin endpoints, CORS | ✅ Done |
| 7 | **Demonstrator** | Frontend–backend integration, end-to-end testing, Codespaces deployment | ✅ Done |

---

## Project Summary

QTD fulfils all FHNW Internet Technology group project requirements:

- ✅ **Multi-device responsive web application** (mobile + desktop via Budibase)
- ✅ **Consistent visual appearance** across all views
- ✅ **List views** for categories (public) and CRUD tables (admin)
- ✅ **Edit and create views** for category and question management
- ✅ **Admin login** with Basic Authentication
- ✅ **Two-tier, three-layer architecture** (Frontend / Controller / Service / Repository / Database)
- ✅ **4+ entities** in the database schema (AppUser, Category, Question, Session, SessionAnswer)
- ✅ **4+ distinct views** (Home, Session Setup, Session Play, Session Summary, Admin panels)
- ✅ **Business logic** with two enforced business rules
- ✅ **OpenAPI 3.0 documentation** via SpringDoc / Swagger UI
- ✅ **GitHub version control** throughout the project
- ✅ **GitHub Codespaces** deployment configuration
- ✅ **OOP, design patterns, DRY, CRUD paradigm** applied throughout
