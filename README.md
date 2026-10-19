# 🎓 EducationCRM

> A secure, role-based education management platform for students and administrators.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-Enabled-6db33f?logo=springsecurity)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Server--side%20UI-005F0F?logo=thymeleaf)](https://www.thymeleaf.org/)
[![License](https://img.shields.io/badge/License-Educational-lightgrey)](#license)

🔗 **Repository:** [github.com/atharvaughade/education-crm-project](https://github.com/atharvaughade/education-crm-project)

---

## ✨ What is EducationCRM?

EducationCRM is a Spring Boot-based education management application that connects students, administrators, courses, enrollments, payments, inquiries, and application settings in one system.

The project demonstrates how a real-world monolithic Spring MVC application can be designed using:

- Layered architecture
- Spring Data JPA
- MySQL
- Thymeleaf
- Spring Security
- BCrypt password hashing
- Role-based authorization
- Form validation
- Global exception handling
- Transactional business services

---

## 🧭 Project Navigator

| Area | Description |
|---|---|
| 👨‍🎓 Student Portal | Registration, login, profile, course browsing, enrollment and learning |
| 🛠️ Admin Panel | Course, student, enrollment, inquiry and settings management |
| 🔐 Security | Session authentication, BCrypt, CSRF and role-based access |
| 💳 Checkout | Enrollment checkout and local payment record creation |
| 🗄️ Persistence | JPA/Hibernate repositories with MySQL |
| 🧪 Testing | Automated Spring Boot tests with isolated test configuration |

---

## 🧱 System Architecture

```mermaid
flowchart LR
    User[Student or Admin] --> Browser[Browser]
    Browser --> UI[Thymeleaf UI]
    UI --> Security[Spring Security Filter Chain]

    Security --> Controllers[Spring MVC Controllers]
    Controllers --> Services[Service Layer]
    Services --> Repositories[Spring Data JPA Repositories]
    Repositories --> Hibernate[Hibernate ORM]
    Hibernate --> Database[(MySQL Database)]

    Services --> Exceptions[Custom Exceptions]
    Exceptions --> Advice[Global Controller Advice]
    Advice --> ErrorPage[User-Friendly Error Page]
