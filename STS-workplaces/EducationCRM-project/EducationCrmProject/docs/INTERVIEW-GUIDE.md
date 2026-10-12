# Interview guide

## Two-minute project explanation

Education CRM is a server-rendered Spring Boot application with two roles. Students register, authenticate, browse courses, complete a simulated checkout, track progress, manage their profile, and submit feedback. Administrators manage the catalog, students, employees, enrollments, leads, feedback, sales, and application settings. The code uses layered MVC architecture, Spring Data JPA for persistence, transactional services for business rules, Thymeleaf for views, and Spring Security for database-backed authentication and role authorization.

## Concepts to be ready to explain

### Dependency injection

Spring creates components and supplies constructor dependencies. Constructor injection exposes required collaborators, supports immutability, and makes unit testing easier than hidden field injection.

### MVC

The controller accepts HTTP input and selects a view. The model contains the data rendered by Thymeleaf. Business decisions live in services so controllers stay focused on the web layer.

### DTO versus entity

A DTO models a specific form or use case; an entity models persisted state. Separate DTOs prevent accidental mass assignment and allow validation rules that do not belong to the database model, such as password confirmation.

### Repository abstraction

Spring Data derives queries from repository method names and generates implementations. The application receives typed persistence operations without writing repetitive JDBC code.

### Transaction

`@Transactional` defines one atomic unit of work. During checkout, the payment and enrollment must both commit or both roll back. Read-only transactions communicate intent and can reduce persistence overhead.

### Authentication versus authorization

Authentication answers “who are you?” by checking email and BCrypt password. Authorization answers “what may you do?” by enforcing `ROLE_ADMIN` or `ROLE_STUDENT` on URL groups.

### CSRF

CSRF attacks cause a logged-in browser to send an unwanted state-changing request. Spring Security validates a per-session token on forms. GET routes do not mutate state, and delete/deactivate operations use POST.

### BCrypt

BCrypt is a salted, adaptive password hash. Strength 12 deliberately makes guesses expensive. Login checks a raw candidate against the stored hash; decryption is neither possible nor necessary.

### Lazy loading and Open Session in View

Open Session in View is disabled to keep database work inside the service layer. Controllers obtain all data needed by a view while the service transaction is active, avoiding hidden template-time queries.

### Soft delete

Deactivation preserves historical enrollment and sales references. Physically deleting a student or course could violate foreign keys or erase business history.

### Optimistic constraints

The service checks for duplicate enrollment for a friendly error, while the database unique constraint is the final defense against concurrent duplicate writes.

## Likely questions

**Why Thymeleaf instead of a REST frontend?**  
It keeps this portfolio project deployable as one application, demonstrates MVC and Spring Security form workflows, and avoids unnecessary client-state complexity. A REST API could be added later for mobile or SPA clients.

**Why H2 tests when production uses MySQL?**  
H2 gives fast, isolated tests with no external dependency. Database-specific behavior should additionally be covered by MySQL integration tests or Testcontainers in CI.

**Why no real payment gateway?**  
Storing or processing card data incorrectly would be unsafe. The current workflow proves transaction design with non-sensitive simulated records. A real system would use provider-hosted collection and signed webhooks.

**How would you scale it?**  
Add pagination and indexed searches, cache stable catalog data, externalize sessions or use stateless APIs where appropriate, move files to object storage, queue email/report work, add observability, and run multiple instances behind a load balancer.

**What would you improve next?**  
Versioned migrations, Testcontainers, email verification/password reset, audit logs, pagination, richer reporting, object storage, CI/CD, and a gateway integration with idempotent webhooks.

## Demonstration order

1. Register and log in as a student.
2. Browse a course and complete checkout.
3. Update learning progress and submit feedback.
4. Log in as admin and show metrics, enrollment/payment, and feedback response.
5. Disable enrollment in settings and show checkout is blocked.
6. Show the security configuration, transactional checkout service, entities, and tests.
