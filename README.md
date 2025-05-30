#User Management System - Spring Boot Microservice
--------------------------------------------------
This is a secure User Management REST API built with **Spring Boot**, **Spring Security**, and **JWT authentication**. 
It supports role-based access control and provides secured endpoints for user-related operations.

##Features
----------
- User Registration and Login using JWT
- Role-based Authorization (`ROLE_USER`, `ROLE_ADMIN`)
- JWT validation on protected endpoints
- Secured REST APIs
- Global Exception Handling using `@RestControllerAdvice`

## Endpoints Overview

### Authentication (Public)

- `POST /register`  
  Registers a new user. By default, assigns `ROLE_USER`.

- `POST /login`  
  Logs in with valid email and password, returns a JWT token.

### User APIs (Secured with JWT)

- `GET /users`  
  Accessible only by users with `ROLE_ADMIN`.

- `GET /users/{id}`  
  Accessible by both `ROLE_USER` and `ROLE_ADMIN`.

- `DELETE /users/{id}`  
  Only accessible by users with `ROLE_ADMIN`.

All secured endpoints require a valid JWT in the `Authorization` header:

##Roles and Access Control
--------------------------
- Upon registration, every user is assigned the `ROLE_USER`.
- Role-based method access is implemented using Spring Security annotations like `@PreAuthorize`.

##Technology Stack
------------------
- Java 17+
- Spring Boot 3.x
- Spring Security
- JWT (JJWT)
- Spring Data JPA
- H2
- Lombok
- Maven

##How to Run
##Prerequisites

- Java 17+
- Maven
- (Optional) MySQL if you want to switch from H2

### Steps
1. Clone the project:
git clone https://github.com/your-username/user-management-system.git

##The application will start on http://localhost:8989
