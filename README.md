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
