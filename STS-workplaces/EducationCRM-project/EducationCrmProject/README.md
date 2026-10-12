# Education CRM

Education CRM is a complete Spring Boot MVC application for managing an education business. It provides separate administrator and student experiences, database-backed authentication, course enrollment, simulated payments, progress tracking, inquiries, feedback, employees, sales reporting, and runtime settings.

## Highlights

- Role-based Spring Security with `ADMIN` and `STUDENT` authorities
- BCrypt password hashing, CSRF protection, session-fixation protection, and POST-only mutations
- Student registration, profile/password management, course catalog, checkout, learning progress, and feedback
- Admin dashboard, students, courses, employees, enrollments, inquiries, sales, feedback responses, profile, and settings
- MySQL for local/runtime data and isolated H2 databases for automated tests
- Server-side validation, service-layer transactions, duplicate protection, and soft deactivation
- Responsive Thymeleaf UI with reusable fragments

## Technology

- Java 17
- Spring Boot 4.1
- Spring MVC, Spring Data JPA, Spring Security, Bean Validation
- Thymeleaf, Bootstrap 5, MySQL, Maven
- JUnit 5, MockMvc, Spring Security Test, H2

## Run locally

1. Create a MySQL database:

   ```sql
   CREATE DATABASE education_crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Configure `src/main/resources/application-local.properties` (this file is ignored by Git):

   ```properties
   spring.datasource.password=your_mysql_password
   ```

   Override any database value with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

3. Optionally bootstrap the first admin by setting these environment variables for one startup:

   ```text
   APP_BOOTSTRAP_ADMIN_ENABLED=true
   APP_ADMIN_EMAIL=admin@example.com
   APP_ADMIN_PASSWORD=ChangeMe@123
   APP_ADMIN_MOBILE=9876543210
   ```

   Disable `APP_BOOTSTRAP_ADMIN_ENABLED` after the account is created.

4. Start the application from STS or run:

   ```powershell
   mvn spring-boot:run
   ```

5. Open `http://localhost:8080`.

## Test

```powershell
mvn test
```

The suite verifies application startup, authentication and authorization boundaries, CSRF enforcement, all main Thymeleaf routes, enrollment/payment creation, duplicate-enrollment prevention, and the enrollment feature toggle.

## Main routes

| Area | Route | Access |
|---|---|---|
| Public site | `/` | Public |
| Login / registration | `/auth/login`, `/auth/signup` | Public |
| Student workspace | `/student/**` | `STUDENT` |
| Admin workspace | `/admin/**` | `ADMIN` |

Checkout is a portfolio-safe payment simulation. It stores a generated reference, method, amount, and status; it never collects or stores card credentials.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Security](docs/SECURITY.md)
- [Database model](docs/DATABASE.md)
- [Runbook](docs/RUNBOOK.md)
- [Interview guide](docs/INTERVIEW-GUIDE.md)

The project intentionally has not been initialized or pushed to GitHub yet. Repository creation is the final phase after local review.
