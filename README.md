# spring-Security-Project

This project demonstrates the integration of **Spring Boot** with **Spring Security** and **JWT (JSON Web Tokens)** for authentication and authorization. It provides a secure backend for an e-commerce application with user management, role-based access control, and protected REST APIs.

---

## Features
- User registration and login
- JWT-based authentication
- Role-based authorization (e.g., Admin, User)
- Secure REST endpoints
- Example controllers for `Student` and `Users`
- Custom `UserDetailsService` implementation
- Centralized security configuration

---

##  Tech Stack
- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **JWT**
- **Maven**
- **H2 / MySQL** (configurable in `application.properties`)

---

## Getting Started

## 1. Clone the repository
```bash
git clone https://github.com/Sharad1606/spring-Security-Project.git
cd spring-Security-Project

2. Build the project
mvn clean install

---

**Authentication Flow**

- Register a new user via /users/register.
- Login via /users/login → returns a JWT token.
- Use the JWT token in the Authorization header for secured endpoints:
Authorization: Bearer <your_token>

---

**Configuration**
Edit src/main/resources/application.properties to configure:
- Database connection (H2/MySQL/Postgres)
- JWT secret key
- Server port

---

**Contributing**
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.






