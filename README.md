# Interview Scheduler API

[![Java](https://img.shields.io/badge/Java-21-red)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-brightgreen)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)]()
[![Maven](https://img.shields.io/badge/Maven-Build-orange)]()
[![Status](https://img.shields.io/badge/Status-In_Development-yellow)]()

---

## About

Interview Scheduler API is a backend application designed to simulate a real-world interview scheduling system.

The project focuses on building a structured REST API with clear business rules, clean architecture, and production-ready patterns.

It is part of my backend portfolio, aiming to demonstrate practical experience with Java, Spring Boot, database integration, and API design.

---

## Technologies

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security

### Database
- PostgreSQL

### Tools
- Maven
- Lombok
- Git
- GitHub

---

## Project Structure

```text
src/main/java/com/thiagomf/interviewschedulerapi
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
└── service
Current Features
User registration
Role-based user structure (Recruiter / Candidate)
Email uniqueness validation
Global exception handling
Basic security configuration
PostgreSQL integration
API Endpoint
Register User
POST /api/auth/register
Request
{
  "name": "Thiago",
  "email": "thiago@email.com",
  "password": "123456",
  "role": "RECRUITER"
}
Response
{
  "id": 1,
  "name": "Thiago",
  "email": "thiago@email.com",
  "role": "RECRUITER"
}
Next Steps
JWT authentication
Login endpoint
Interview entity
Scheduling system
Time conflict validation
Interview status flow
Swagger documentation
Project Goal

This project aims to represent a backend system closer to a real company scenario, going beyond simple CRUD operations.

The focus is on building a clean, organized, and scalable API with real business rules and professional structure.

Author

Thiago Machado Freire

GitHub: https://github.com/ThiagoMF1

LinkedIn: https://www.linkedin.com/in/thiago-machado-freire-779964281/
