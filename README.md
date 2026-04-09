# Interview Scheduler API

[![Java](https://img.shields.io/badge/Java-21-black?style=for-the-badge)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-black?style=for-the-badge)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-black?style=for-the-badge)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge)](https://jwt.io/)
[![Maven](https://img.shields.io/badge/Maven-Build-black?style=for-the-badge)](https://maven.apache.org/)

## Overview

Interview Scheduler API is a RESTful backend application designed to manage technical interview processes between recruiters and candidates.

This project focuses on real-world backend practices such as authentication, business rules, scheduling validation, and secure access control.

## Features

- User registration and authentication (JWT)
- Role-based access (Recruiter / Candidate)
- Create interview scheduling
- Validate future dates and allowed durations
- Prevent schedule conflicts
- List interviews for authenticated user
- Filter interviews by status
- Pagination support
- Retrieve interview by ID with access control
- Cancel interview with reason
- Complete interview with validation rules
- Dashboard with interview metrics
- Global exception handling

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- PostgreSQL
- Maven

## Project Structure


src/main/java/com/thiagomf/interviewschedulerapi
│
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service


## API Endpoints

### Auth

| Method | Endpoint              | Description              |
|--------|----------------------|--------------------------|
| POST   | /api/auth/register   | Register user            |
| POST   | /api/auth/login      | Login and get token      |
| GET    | /api/auth/me         | Get current user         |

### Interviews

| Method | Endpoint                              | Description                    |
|--------|----------------------------------------|--------------------------------|
| POST   | /api/interviews                        | Create interview               |
| GET    | /api/interviews/me                     | List my interviews             |
| GET    | /api/interviews/{id}                   | Get interview by ID            |
| PATCH  | /api/interviews/{id}/cancel            | Cancel interview               |
| PATCH  | /api/interviews/{id}/complete          | Complete interview             |

### Dashboard

| Method | Endpoint              | Description              |
|--------|----------------------|--------------------------|
| GET    | /api/dashboard       | Interview metrics         |

## Query Parameters

### List Interviews


GET /api/interviews/me?status=SCHEDULED&page=0&size=10


- status: SCHEDULED | COMPLETED | CANCELED
- page: page number
- size: items per page

## Authentication

All protected routes require JWT token:


Authorization: Bearer <your_token>


## Business Rules

- Only recruiters can schedule interviews
- Only candidates can be selected as candidates
- Interview duration must be 30 or 60 minutes
- Interview date must be in the future
- Schedule conflicts are not allowed
- Only participants can access the interview
- Only scheduled interviews can be canceled or completed
- Only recruiter can complete an interview

## Database

PostgreSQL database configuration:


spring.datasource.url=jdbc:postgresql://localhost:5432/interview_scheduler_db
spring.datasource.username=postgres
spring.datasource.password=123456


## Running the Project

```bash
./mvnw spring-boot:run
Example Request
Create Interview
{
  "candidateId": 14,
  "scheduledAt": "2026-04-06T15:00:00",
  "durationMinutes": 30,
  "notes": "Backend interview"
}
Author

Thiago Machado Freire
https://github.com/ThiagoMF1
