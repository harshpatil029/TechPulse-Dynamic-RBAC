# Dynamic RBAC Authorization Engine — Starter

A Java 21 / Spring Boot starter for the TechPulse IT Dynamic RBAC assignment.

## Included
- H2 file-backed database (data persists under `./data`)
- JPA entities: User, Role, Permission, UserRole, RolePermission
- HTTP Basic authentication with BCrypt password hashing
- `@EnableMethodSecurity` and custom `PermissionEvaluator`
- The five assignment APIs, plus public user registration

## Run
Requirements: JDK 21 and Maven 3.9+.

```bash
mvn spring-boot:run
```

H2 console: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:file:./data/rbacdb`  
Username: `sa`  
Password: (blank)

## API examples

Register a user (public):
```http
POST http://localhost:8080/users/register
Content-Type: application/json

{"username":"admin","password":"ChangeMe123!"}
```

The assignment intentionally does not seed hardcoded roles or permissions. For the first administrator only, create the initial records in the local H2 console (development bootstrap), then use the APIs to manage subsequent records. First register the admin account above, then execute:

```sql
INSERT INTO roles(name) VALUES ('ADMIN');
INSERT INTO permissions(name) VALUES ('ROLE_CREATE');
INSERT INTO permissions(name) VALUES ('PERMISSION_CREATE');
INSERT INTO permissions(name) VALUES ('ROLE_PERMISSION_ASSIGN');
INSERT INTO permissions(name) VALUES ('USER_ROLE_ASSIGN');
INSERT INTO permissions(name) VALUES ('SECURE_DATA_READ');

INSERT INTO user_roles(user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ADMIN';

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'ADMIN';
```

After that, call APIs with HTTP Basic authentication using `admin` and the password you registered.

Create a regular user via `/users/register`, then assign a role using the protected assignment API. A role only grants the permissions mapped to it.

## Required endpoints
| Method | Path | Permission |
|---|---|---|
| POST | `/roles` | `ROLE_CREATE` |
| POST | `/permissions` | `PERMISSION_CREATE` |
| POST | `/roles/{roleId}/permissions/{permissionId}` | `ROLE_PERMISSION_ASSIGN` |
| POST | `/users/{userId}/roles/{roleId}` | `USER_ROLE_ASSIGN` |
| GET | `/secure-data` | `SECURE_DATA_READ` |

## Important notes
- No `hasRole` / `hasAuthority` role-based authorization is used. The method checks call the custom database-backed permission evaluator.
- The `AUTHENTICATED` authority is only a marker required by Spring's user model; it is not used for access decisions.
- The H2 console is enabled for local development only. Disable it and use a production database before deployment.
- This is a starter scaffold, not a finished submission. Add integration/unit tests for success, unauthenticated, forbidden, duplicate, and not-found cases; verify the 80% coverage target. Add exception handling, DTOs, pagination, and stronger validation before final submission.
- Do not expose H2 console or HTTP Basic over an untrusted network.
