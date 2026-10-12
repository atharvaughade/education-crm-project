# Local runbook

## Prerequisites

- JDK 17
- Maven 3.9+
- MySQL 8+
- STS/Eclipse or another Java IDE

## First startup

1. Create the `education_crm` database.
2. Put the local MySQL password in `src/main/resources/application-local.properties` or set `DB_PASSWORD`.
3. Set the one-time admin bootstrap variables described in the root README.
4. Run `EducationCrmProjectApplication` from STS.
5. Confirm `Started EducationCrmProjectApplication` appears in the console.
6. Open `http://localhost:8080/admin/login` and sign in.
7. Turn off the bootstrap flag after the first admin exists.

## Configuration reference

| Variable | Default | Meaning |
|---|---|---|
| `SERVER_PORT` | `8080` | HTTP port |
| `DB_URL` | local `education_crm` URL | JDBC connection URL |
| `DB_USERNAME` | `root` | Database user |
| `DB_PASSWORD` | empty | Database password; local profile can override it |
| `JPA_DDL_AUTO` | `update` | Hibernate schema behavior |
| `THYMELEAF_CACHE` | `false` | Template caching for local development |
| `APP_BOOTSTRAP_ADMIN_ENABLED` | `false` | Enables one-time first-admin creation |

## Common commands

```powershell
mvn clean test
mvn spring-boot:run
mvn clean package
java -jar target\educationcrm-1.0.0.jar
```

## Troubleshooting

- **Database connection refused:** start MySQL and verify `DB_URL`, port, and database name.
- **Access denied for database user:** verify the username/password and MySQL grants.
- **Port already in use:** set `SERVER_PORT=8081` before starting.
- **Cannot log in:** verify the account is active; bootstrap only creates an admin when the configured email does not already exist.
- **Templates look stale:** keep `THYMELEAF_CACHE=false` during development and restart STS after a full Maven refresh.
- **Tests should not use MySQL:** they automatically activate the `test` profile and use H2.

## Safe GitHub phase

Do not commit `application-local.properties`, `.env`, logs, generated `target` folders, or IDE metadata. Before the first push, run tests, inspect `git status`, scan for secrets, and then create the remote repository. That phase is intentionally separate from implementation.
