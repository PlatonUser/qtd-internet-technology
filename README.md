# internet-technology-qtd
QTD helps users start meaningful conversations by offering themed question sessions in a simple web application. It is useful for friends, couples, or groups who want guided discussion topics.
## Contents

- [Analysis](#analysis)
  - [Scenario](#scenario)
  - [User Stories](#user-stories)
  - [Use Cases](#use-cases)

- [Design](#design)
  - [Prototype Design](#prototype-design)
  - [Domain Design](#domain-design)
  - [Business Logic](#business-logic)
  - [Database Schema](#database-schema)

- [Implementation](#implementation)
  - [Backend Technology](#backend-technology)
  - [Frontend Technology](#frontend-technology)

- [Project Management](#project-management)
  - [Roles](#roles)
  - [Milestones](#milestones)
# Analysis

## Scenario

QTD (Questions To Discuss) is a web-based application designed to help users start meaningful conversations through guided question sessions. In many social situations such as meeting friends, dating, or spending time with a group, people often struggle to initiate interesting or deeper discussions. QTD addresses this problem by providing themed categories of questions that guide users through structured conversation sessions.

Users can open the application, select a category (for example friends, dating, deep conversations, or fun topics), and start a session where questions are displayed one by one. During the session, users can optionally enter short answers or reflections. At the end of the session, the application provides a summary showing all questions and the responses entered during the session.

The system is implemented as a responsive web application that works on both desktop and mobile devices. It integrates a frontend user interface, a backend REST API, and a database that stores categories, questions, users, and session data.

In addition to public users who participate in question sessions, the system includes an administrator role. Administrators can log in and manage the content of the application, such as categories and questions, ensuring that the system remains relevant and well-maintained.

The main value of QTD is to support meaningful conversations in a simple and accessible way, while demonstrating a complete web application architecture consisting of frontend, backend, and database layers.

---

## Actors

The system contains two main actors:

**Public User**
- Browses available question categories
- Starts question sessions
- Answers questions during the session
- Views a summary of the session

**Administrator**
- Logs into the system
- Manages categories
- Manages questions
- Views session data

---

## Use Cases

### Public User Use Cases

**Browse Categories**
- The user opens the application and sees a list of available question categories.

**Start Session**
- The user selects a category and starts a question session.

**View Questions**
- The system displays questions one by one during the session.

**Enter Answers**
- The user can optionally enter short answers or reflections for each question.

**View Session Summary**
- At the end of the session, the system displays a summary containing the questions and the user's answers.

---

### Administrator Use Cases

**Log In**
- The administrator logs into the system using authentication.

**Manage Categories**
- The administrator can create, edit, or delete categories.

**Manage Questions**
- The administrator can create, edit, or delete questions assigned to categories.

**View Sessions**
- The administrator can view existing sessions and session summaries.

---

## User Stories

### Administrator User Stories

As an administrator, I want to log in to the system so that I can securely manage the application.

As an administrator, I want to view categories in a list so that I can manage existing discussion topics.

As an administrator, I want to create and edit categories so that I can organize questions into meaningful groups.

As an administrator, I want to create and edit questions so that I can maintain the discussion content.

As an administrator, I want to delete outdated categories or questions so that the application remains relevant.

As an administrator, I want to view sessions so that I can monitor how the application is used.

---

### Public User Stories

As a user, I want to browse question categories so that I can choose a topic for a conversation.

As a user, I want to start a question session so that I can explore discussion topics with others.

As a user, I want to see questions one by one so that the conversation flows naturally.

As a user, I want to optionally write answers during a session so that I can reflect on the questions.

As a user, I want to see a summary of the session so that I can review all questions and answers at the end.

---

# Design

## Database Schema

The QTD application uses a relational database with five main entities that store categories, questions, user sessions, and responses. The database is designed to support the core functionality of managing discussion categories, displaying questions, recording user sessions, and storing user answers.

### Entity Overview

**AppUser**
- Stores administrator credentials and role information
- Fields: id, username (unique), password, role

**Category**
- Represents a discussion category grouping related questions
- Fields: id, name, slug (unique), description, icon, color, active
- Example categories: Friends, Dating, Deep Talk, Fun Topics

**Question**
- Individual questions belonging to a category
- Fields: id, text, category_id (foreign key), active
- Each category can contain multiple questions

**Session**
- Represents a user's discussion session within a category
- Fields: id, category_id (foreign key), started_at, completed
- Tracks when a session begins and whether it has been completed

**SessionAnswer**
- Stores user answers provided during a session
- Fields: id, session_id (foreign key), question_id (foreign key), answer_text, answered_at
- Links sessions to specific questions and captures user responses

---

### Entity Relationships

```
Category (1) ──── (*) Question
Category (1) ──── (*) Session
Question (1) ──── (*) SessionAnswer
Session (1) ──── (*) SessionAnswer
```

**Relationship Details:**

- **Category → Question** (One-to-Many): Each category contains multiple questions. Deleting a category cascades to all its questions.

- **Category → Session** (One-to-Many): Each category has multiple sessions. Users select a category to start a new session.

- **Question → SessionAnswer** (One-to-Many): Each question can be answered multiple times across different sessions.

- **Session → SessionAnswer** (One-to-Many): Each session contains answers for multiple questions in that category.

---

### Database Table Structure

| Table | Columns | Key Constraints |
|-------|---------|-----------------|
| **app_user** | id (PK), username (UK), password, role | Primary Key: id, Unique: username |
| **category** | id (PK), name, slug (UK), description, icon, color, active | Primary Key: id, Unique: slug |
| **question** | id (PK), text, category_id (FK), active | Primary Key: id, Foreign Key: category_id → category.id |
| **session** | id (PK), category_id (FK), started_at, completed | Primary Key: id, Foreign Key: category_id → category.id |
| **session_answer** | id (PK), session_id (FK), question_id (FK), answer_text, answered_at | Primary Key: id, Foreign Keys: session_id → session.id, question_id → question.id |

---

### Data Initialization

The application seeds the database with sample data including:
- **4 Categories**: Friends, Dating, Deep Talk, Fun Topics
- **20 Questions**: 5 questions per category
- **Sample Sessions**: 2 example sessions with user answers

This seed data is defined in `src/main/resources/data.sql` and is automatically loaded when the application starts.

### Wireframe & Prototype

The UI was first prototyped in **Budibase** to establish screen layouts, colour system, and navigation flow before implementation. Key screens designed:

- `/` – Home: category cards grid with "Start Session" buttons
- `/session/setup` – Player setup: enter player names before starting
- `/session/play` – Session view: question-by-question with answer inputs
- `/session/play/final` – Session view: all questions are answered, button "finish" appear
- `/admin/login` – Admin login form
- `/admin/dashboard` – Stats overview + category breakdown table
- `/admin/categories` – Category CRUD table
- `/admin/questions` – Question CRUD table
- `/admin/sessions` – Sessions read-only list


**Rule 1 – Minimum questions required to start a session**

A session can only be started if the chosen category has **at least 3 active questions**. 


**Rule 2 – Cannot delete a category with active questions**

An admin cannot delete a category that still has active questions. 

---

# internet-technology-qtd

QTD (Questions To Discuss) is a web-based application that helps users start meaningful conversations through guided question sessions. It provides themed categories such as friends, dating, deep talk, and fun topics, allowing users to explore structured discussions in an intuitive and visually engaging interface.

The application follows a **two-tier, three-layer architecture** consisting of:

* Frontend (Budibase UI)
* Backend (Spring Boot REST API)
* Database (Relational schema with multiple entities)

---

# Contents

* [Analysis](#analysis)
* [Design](#design)
* [API Design](#api-design)
* [Implementation](#implementation)
* [Project Management](#project-management)

---

# Analysis

## Scenario

QTD addresses a common problem: people often struggle to initiate meaningful conversations in social settings. Whether meeting friends, going on a date, or spending time in a group, conversations can feel repetitive or shallow.

This application solves that by offering structured, themed question sessions. Users select a category and are guided through questions one by one. At the end, they receive a summary of all questions and their responses.

The system is accessible on both desktop and mobile devices and is designed with a consistent and modern user interface.

---

## Actors

### Public User

* Browse categories
* Start sessions
* Answer questions
* View summary

### Administrator

* Log in
* Manage categories
* Manage questions
* View sessions

---

## Use Cases

### Public User

* Browse Categories
* Start Session
* Answer Questions
* View Summary

### Administrator

* Log In
* Manage Categories (CRUD)
* Manage Questions (CRUD)
* View Sessions

---

## User Stories

### Admin

* As an admin, I want to log in so that I can securely manage the system
* As an admin, I want to manage categories so that I can organize content
* As an admin, I want to manage questions so that content stays relevant

### User

* As a user, I want to browse categories so that I can choose a topic
* As a user, I want to start a session so that I can explore questions
* As a user, I want to answer questions so that I can reflect
* As a user, I want to see a summary so that I can review the session

---

# Design

## Architecture

The application follows:

* **3 Layers**

  * Controller (API)
  * Service (Business logic)
  * Repository (Data access)

* **2 Tiers**

  * Frontend (Budibase)
  * Backend (Spring Boot)

---

## Database Schema

### Entities

**AppUser**

* id, username, password, role

**Category**

* id, name, slug, description, icon, color, active

**Question**

* id, text, category_id, active

**Session**

* id, category_id, started_at, completed

**SessionAnswer**

* id, session_id, question_id, answer_text

---

## Relationships

* Category → Question (1:N)
* Category → Session (1:N)
* Session → SessionAnswer (1:N)
* Question → SessionAnswer (1:N)

---

## Business Logic

### Rule 1 – Minimum Questions

A session can only start if a category has at least **3 active questions**

### Rule 2 – Safe Delete

A category cannot be deleted if it contains active questions

---

## Frontend Design (Budibase)

The UI is implemented using **Budibase**, following a consistent design system:

### Public Views

* `/` – Category list (cards)
* `/session/setup` – Player setup
* `/session/play` – Question flow
* `/session/play/final` – Summary view

### Admin Views

* `/admin/login`
* `/admin/categories`
* `/admin/questions`
* `/admin/sessions`

The frontend consumes REST APIs dynamically — no hardcoded data.

---

# API Design

## Overview

The backend exposes a REST API structured into:

* **Public endpoints** → no authentication
* **Admin endpoints** → require Basic Auth

---

## Public Endpoints

| Method | Endpoint                         | Description                    |
| ------ | -------------------------------- | ------------------------------ |
| GET    | /api/categories                  | Get all active categories      |
| GET    | /api/categories/{id}             | Get category by ID             |
| GET    | /api/categories/{id}/questions   | Get questions by category      |
| GET    | /api/sessions/start/{categoryId} | Start session (validates rule) |
| POST   | /api/sessions                    | Submit session answers         |
| GET    | /api/sessions/{id}               | Get session summary            |

---

## Admin Endpoints

| Method | Endpoint                   |
| ------ | -------------------------- |
| GET    | /api/admin/categories      |
| POST   | /api/admin/categories      |
| PUT    | /api/admin/categories/{id} |
| DELETE | /api/admin/categories/{id} |

| Method | Endpoint                  |
| ------ | ------------------------- |
| GET    | /api/admin/questions      |
| GET    | /api/admin/questions/{id} |
| POST   | /api/admin/questions      |
| PUT    | /api/admin/questions/{id} |
| DELETE | /api/admin/questions/{id} |

| Method | Endpoint                 |
| ------ | ------------------------ |
| GET    | /api/admin/sessions      |
| DELETE | /api/admin/sessions/{id} |

---

## DTO Structure

### CategoryDto

* id, name, slug, description, icon, color, active

### QuestionDto

* id, text, categoryId, active

### SessionDto

* id, categoryId, startedAt, completed, answers[]

### SessionRequest

* categoryId
* answers[]

### SessionAnswerDto

* questionId, answerText

---

## Authentication

* Public endpoints → no auth
* Admin endpoints → Basic Auth required
* Role: ADMIN

---

## Error Handling

| Code | Meaning                 |
| ---- | ----------------------- |
| 400  | Validation error        |
| 401  | Unauthorized            |
| 404  | Not found               |
| 409  | Business rule conflict  |
| 422  | Business rule violation |
| 500  | Server error            |

---

## OpenAPI / Swagger

The API is documented using **SpringDoc OpenAPI**.

Access via:

```
/swagger-ui.html
```

---

# Implementation

## Backend

* Java 17
* Spring Boot 3
* Spring Data JPA
* H2 Database
* Spring Security (Basic Auth)
* OpenAPI (Swagger)

---

## Frontend

* Budibase (low-code)
* REST API integration
* Responsive UI
* Component-based layout

---

## Running the Project

```bash
cd backend
mvn spring-boot:run
```

Then open:

```
http://localhost:8080
```

---

## Codespaces Deployment

1. Start Codespace
2. Wait for backend to run
3. Make port 8080 public
4. Open provided URL

---

# Project Management

## Roles

* Backend / Data: Entities, services
* Frontend: Budibase UI
* DevOps / API: Controllers, security, deployment

---

## Milestones

| Milestone     | Status      |
| ------------- | ----------- |
| Analysis      | Done        |
| Domain Design | Done        |
| Frontend      | Done        |
| API Design    | Done        |
| Backend       | In Progress |
| Security      | Pending     |
| Integration   | Pending     |

---

# Final Notes

This project fulfills all FHNW requirements:

* Multi-layer architecture
* 4+ views
* REST API
* Business logic
* Authentication
* OpenAPI documentation
* Working demonstrator

---

