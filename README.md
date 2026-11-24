# TechPulse Dynamic Role-Based Access Control (RBAC)

A Spring Boot application implementing **Dynamic Role-Based Access Control (RBAC)** with JWT-based authentication and database-driven authorization.

The application allows users to register and authenticate, create roles and permissions, assign permissions to roles, and assign roles to users. Access to protected APIs is controlled dynamically based on permissions stored in the database.

## 1. Technology Stack

- Java 21
- Spring Boot 3.4.1
- Spring Security 6
- JWT Authentication
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Springdoc OpenAPI / Swagger UI
- Jakarta Bean Validation

## 2. Key Features

- User registration with BCrypt password encryption.
- JWT-based authentication.
- Stateless authentication using Spring Security.
- Dynamic role creation.
- Dynamic permission creation.
- Assign permissions to roles.
- Assign roles to users.
- Database-driven permission validation.
- Method-level authorization using `@PreAuthorize`.
- Custom `PermissionEvaluator`.
- Swagger UI integration with Bearer Token authentication.
- H2 database persistence.

## 3. Project Structure

```text
src/main/java/com/example/rbac/
│
├── config/
│   └── OpenApiConfig.java
│
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── RoleController.java
│   ├── PermissionController.java
│   └── SecureController.java
│
├── dto/
│
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Permission.java
│   ├── UserRole.java
│   └── RolePermission.java
│
├── repository/
│
├── security/
│   ├── SecurityConfig.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   ├── CustomUserDetailsService.java
│   └── CustomPermissionEvaluator.java
│
├── service/
│   └── ...
│
└── exception/
    └── ...

src/main/resources/
└── application.properties
```

## 4. Database Design

The application uses five main entities:

| Entity | Responsibility |
|---|---|
| User | Stores registered users |
| Role | Stores application roles |
| Permission | Stores individual permissions |
| UserRole | Maps users to roles |
| RolePermission | Maps roles to permissions |

### Relationships

- A user can have multiple roles.
- A role can be assigned to multiple users.
- A role can contain multiple permissions.
- A permission can belong to multiple roles.

The relationships are persisted in the H2 database.

## 5. API Endpoints

All endpoints are accessible through the application's Swagger UI.

### Authentication

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| POST | `/users/register` | Register a user | Not required |
| POST | `/auth/login` | Authenticate and obtain JWT | Not required |

### Role Management

| Method | Endpoint | Description | Required Permission |
|---|---|---|---|
| POST | `/roles` | Create a role | `ROLE_CREATE` |
| POST | `/roles/{roleId}/permissions/{permissionId}` | Assign permission to role | `ROLE_PERMISSION_ASSIGN` |

### Permission Management

| Method | Endpoint | Description | Required Permission |
|---|---|---|---|
| POST | `/permissions` | Create a permission | `PERMISSION_CREATE` |

### User Management

| Method | Endpoint | Description | Required Permission |
|---|---|---|---|
| POST | `/users/{userId}/roles/{roleId}` | Assign role to user | `USER_ROLE_ASSIGN` |

### Protected Resource

| Method | Endpoint | Description | Required Permission |
|---|---|---|---|
| GET | `/secure-data` | Access protected data | `SECURE_DATA_READ` |

## 6. Authentication and Authorization Flow

1. A user registers using `/users/register`.
2. The user logs in using `/auth/login`.
3. The application authenticates the credentials and generates a JWT.
4. The client sends the JWT in the Authorization header for protected requests.

```http
Authorization: Bearer <JWT_TOKEN>
```

5. `JwtAuthenticationFilter` extracts and validates the token.
6. Spring Security establishes the authenticated user in the SecurityContext.
7. The `@PreAuthorize` annotation invokes the custom permission evaluator.
8. The evaluator checks the user's roles and their assigned permissions against the database.
9. Access is granted or denied based on the permission mapping.

### Authorization Example

```java
@PreAuthorize("hasPermission(null, 'SECURE_DATA_READ')")
```

The permission evaluator checks whether the authenticated user has the required permission through their assigned roles.

This approach allows permissions to be changed dynamically in the database without hardcoding role names into the authorization logic.

## 7. HTTP Security Responses

| Status Code | Meaning |
|---|---|
| 200 OK | Request successful |
| 400 Bad Request | Invalid request |
| 401 Unauthorized | Missing or invalid authentication |
| 403 Forbidden | Authenticated user lacks the required permission |
| 404 Not Found | Requested resource not found |

## 8. Project Setup and Execution

### Prerequisites

- Java 21
- Maven
- IntelliJ IDEA or another Java IDE

### Clone the Repository

```bash
git clone https://github.com/harshpatil029/TechPulse-Dynamic-RBAC.git
cd TechPulse-Dynamic-RBAC
```

### Configure the JWT Secret

The application requires a JWT secret of at least 32 bytes.

Set the `JWT_SECRET` environment variable before starting the application.

**Windows PowerShell:**

```powershell
$env:JWT_SECRET="replace-with-your-own-secure-secret-of-at-least-32-bytes"
```

Do not commit real secrets or tokens to GitHub.

### Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

### Swagger UI

```text
http://localhost:8080/swagger-ui/index.html
```

Use the Authorize button in Swagger UI to provide the JWT token for protected APIs.

### H2 Database Console

```text
http://localhost:8080/h2-console/
```

Database configuration:

- JDBC URL: `jdbc:h2:file:./data/rbacdb`
- Username: `sa`
- Password: Leave empty unless configured otherwise.

The H2 database is file-based, allowing data to persist between application restarts.

## 9. Assumptions and Design Decisions

- JWT is used for stateless authentication.
- Passwords are stored using BCrypt hashing.
- Roles and permissions are stored in the database rather than hardcoded.
- Users receive permissions through their assigned roles.
- Authorization is implemented using Spring Security method-level security.
- A custom `PermissionEvaluator` performs database-driven permission checks.
- The application uses H2 for lightweight persistence and development.
- Protected APIs require a valid JWT and the corresponding permission.
- Swagger UI is configured to support Bearer Token authentication.

## 10. Testing

Run the test suite using:

```bash
mvn test
```

Run the complete Maven verification lifecycle using:

```bash
mvn clean verify
```



GitHub: https://github.com/harshpatil029/TechPulse-Dynamic-RBAC

---

**Developed as part of the TechPulse IT Services Pvt. Ltd. Java Developer Technical Assignment.**
