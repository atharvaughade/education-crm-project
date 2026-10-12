# Security model

## Authentication

`CustomUserDetailsService` searches the admin store first and then the student store by normalized email. It returns a `CrmUserPrincipal` with exactly one authority: `ROLE_ADMIN` or `ROLE_STUDENT`. Password comparison is performed by Spring Security with BCrypt strength 12; raw passwords are never stored.

`RoleAwareAuthenticationSuccessHandler` sends authenticated users to the correct dashboard. Disabled accounts cannot authenticate.

## Authorization

The `SecurityFilterChain` applies these rules in order:

1. Public pages, authentication endpoints, the inquiry endpoint, error pages, and static assets are permitted.
2. `/admin/**` requires `ROLE_ADMIN`.
3. `/student/**` requires `ROLE_STUDENT`.
4. Every other route requires authentication.

The order matters because Spring Security uses the first matching authorization rule.

## Implemented controls

- CSRF protection remains enabled. Every state-changing form is POST and receives Thymeleaf's generated CSRF token.
- Login uses the framework authentication filter instead of custom password comparison.
- Logout is POST-only and clears authentication, session data, and the session cookie.
- Session fixation protection migrates the session identifier after login.
- Password changes require the current password and invalidate neither hashing strength nor role data.
- User input uses Bean Validation and server-side error handling.
- Role-specific URL namespaces prevent horizontal access between admin and student screens.
- Database passwords live in ignored local configuration or environment variables, not in committed configuration.
- Payment data is deliberately non-sensitive; no card number, CVV, or banking credential is accepted.
- Delete actions are soft deactivations where historical data must remain referentially valid.

## Production hardening checklist

Before internet deployment:

- Serve only over HTTPS and enable secure, HTTP-only, SameSite session cookies.
- Move all credentials to a managed secret store.
- Replace `ddl-auto=update` with reviewed Flyway or Liquibase migrations and use `validate` at runtime.
- Add rate limiting, login throttling/lockout, audit events, centralized logging, and monitoring.
- Add verified email flows and password reset tokens with short expirations.
- Configure content security policy and other HTTP response headers for the deployment domain.
- Run dependency and container vulnerability scans in CI.
- Integrate a payment provider only through hosted fields/pages and verified webhooks.

These deployment controls are intentionally documented rather than faked in a local portfolio application.
