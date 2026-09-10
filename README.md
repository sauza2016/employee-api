# Employee API

REST API for employee management built with Java 17, Spring Boot, MySQL, and Hexagonal Architecture.

The application provides CRUD operations, partial employee search, validation, centralized error handling, Basic Authentication, API documentation, health monitoring, automated tests, Docker support, and continuous integration with GitHub Actions.

## Technologies

- Java 17
- Spring Boot 2.7.x
- Spring Web
- Spring Data JPA / Hibernate
- Spring Security
- Spring Boot Actuator
- MySQL 8
- H2 for automated tests
- Maven
- JUnit 5
- Mockito
- MockMvc
- JaCoCo
- Springdoc OpenAPI / Swagger
- Docker
- Docker Compose
- GitHub Actions
- Postman

## Architecture

The project follows **Hexagonal Architecture (Ports and Adapters)** to separate business logic from external technologies.

```text
                       REST API
                          │
                          ▼
                  EmployeeController
                          │
                          ▼
                  EmployeeUseCase
                          ▲
                          │
                   EmployeeService
                          │
                          ▼
                EmployeeRepositoryPort
                          ▲
                          │
                  EmployeeJpaAdapter
                          │
                          ▼
                EmployeeJpaRepository
                          │
                          ▼
                        MySQL
```

The dependency direction keeps the domain and application layers independent from persistence and HTTP implementation details.

### Main layers

```text
com.invex.employee
├── domain
│   └── model
│
├── application
│   ├── exception
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
│
└── infrastructure
    ├── adapter
    │   ├── in
    │   │   └── rest
    │   └── out
    │       └── persistence
    └── config
```

### Domain

Contains the core employee model and business concepts. It does not depend on Spring, JPA, or HTTP.

### Application

Contains the use cases, application services, ports, and application exceptions.

`EmployeeUseCase` defines the operations exposed by the application.

`EmployeeRepositoryPort` defines the persistence operations required by the application without depending directly on Spring Data JPA.

### Infrastructure

Contains technology-specific implementations, including:

- REST controllers
- Request and response DTOs
- JPA entities
- Spring Data repositories
- Persistence adapters
- Security configuration
- OpenAPI configuration
- Logging
- Exception handling
- Spring bean configuration

## Employee Model

An employee contains the following information:

- ID
- First name
- Middle name
- Paternal last name
- Maternal last name
- Age
- Gender
- Birth date
- Position
- Registration date
- Active status

The registration date is generated automatically when the employee is persisted.

Dates received through the REST API use the format:

```text
dd-MM-yyyy
```

Example:

```text
10-05-1991
```

## REST API

Base URL:

```text
http://localhost:8080
```

| Method | Endpoint | Description |
|---|---|---|
| GET | `/employees` | Get all employees |
| GET | `/employees/{id}` | Get employee by ID |
| POST | `/employees` | Create one or multiple employees |
| PUT | `/employees/{id}` | Update an employee |
| DELETE | `/employees/{id}` | Delete an employee |
| GET | `/employees/search?name={name}` | Search employees by partial name |

The search operation performs a case-insensitive partial search across employee name and surname fields.

## Create Employees

The POST endpoint accepts an array, allowing one or multiple employees to be created in the same request.

Example:

```http
POST /employees
```

```json
[
  {
    "firstName": "John",
    "middleName": "Michael",
    "paternalLastName": "Smith",
    "maternalLastName": "Brown",
    "age": 35,
    "gender": "MALE",
    "birthDate": "10-05-1991",
    "position": "Software Engineer",
    "active": true
  }
]
```

Successful response:

```text
201 Created
```

## Partial Update

The update operation supports changing only the provided fields.

Example:

```http
PUT /employees/1
```

```json
{
  "position": "Technical Lead"
}
```

Fields not included in the request keep their current values.

The employee ID and registration date are not modified by this operation.

## HTTP Status Codes

The API uses standard HTTP status codes.

| Status | Description |
|---|---|
| 200 | Successful request |
| 201 | Employee created |
| 204 | Employee deleted |
| 400 | Invalid request or validation error |
| 401 | Authentication required or invalid credentials |
| 404 | Employee not found |
| 500 | Unexpected server error |

## Validation and Error Handling

Input validation is implemented using Bean Validation.

Examples include:

- Required fields
- Positive age
- Valid date format
- Past birth date
- Field length validation

Errors are handled centrally using a global exception handler.

Error responses contain structured information such as:

```json
{
  "timestamp": "2026-09-10T12:00:00",
  "status": 404,
  "error": "Not Found",
  "code": "EMPLOYEE_NOT_FOUND",
  "message": "Employee not found with id: 999999",
  "path": "/employees/999999"
}
```

Validation errors return HTTP `400 Bad Request`, while missing employees return HTTP `404 Not Found`.

## Security

The API uses **HTTP Basic Authentication** through Spring Security.

Swagger documentation and the Actuator health endpoint can be accessed without authentication.

Employee endpoints require valid credentials.

Development credentials can be configured using environment variables.

Example:

```text
APP_USERNAME=admin
APP_PASSWORD=admin123
```

> The credentials shown above are intended only for local development and demonstration purposes. Production credentials should be provided securely through environment variables or a secrets management system.

## Swagger / OpenAPI

Interactive API documentation is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

Swagger can be used to inspect the API contract and available endpoints.

## Actuator

Spring Boot Actuator is enabled for application monitoring.

Health endpoint:

```text
http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Requirements

For local execution:

- Java 17
- Maven 3.x
- MySQL 8

Alternatively, Docker and Docker Compose can be used to run the complete environment.

## Running Tests

Run all automated tests with:

```bash
mvn clean verify
```

The test suite includes:

- Application service unit tests
- REST controller tests with MockMvc
- Persistence adapter tests
- JPA repository integration tests with H2
- Spring Security tests
- Actuator tests
- Application context test

H2 is used by automated tests so that the test suite does not depend on an external MySQL instance.

## Code Coverage

JaCoCo is integrated with the Maven `verify` lifecycle.

Run:

```bash
mvn clean verify
```

Then open:

```text
target/site/jacoco/index.html
```

Current test coverage is approximately:

```text
Line coverage:    92%
Method coverage:  90%
Class coverage:  100%
```

Coverage is used as a quality indicator together with meaningful behavioral tests rather than as a goal of reaching 100%.

## Running Locally

Create the MySQL database:

```sql
CREATE DATABASE employee_db;
```

Configure the datasource using environment variables:

```text
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/employee_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=employee_user
SPRING_DATASOURCE_PASSWORD=your_password
```

Configure application credentials:

```text
APP_USERNAME=admin
APP_PASSWORD=admin123
```

Build the application:

```bash
mvn clean package
```

Run:

```bash
java -jar target/*.jar
```

The API will be available at:

```text
http://localhost:8080
```

## Docker

The project includes a multi-stage Docker build.

Build and start the complete environment with:

```bash
docker compose up --build
```

Docker Compose starts:

```text
employee-api
    │
    │ JDBC
    ▼
employee-mysql
```

The application is available at:

```text
http://localhost:8080
```

MySQL is exposed to the host at:

```text
localhost:3307
```

The application communicates internally with MySQL using the Docker service name and container port:

```text
mysql:3306
```

MySQL includes a health check, and the API waits until the database service is healthy before starting.

Stop the environment with:

```bash
docker compose down
```

To also remove the database volume:

```bash
docker compose down -v
```

## Postman

A Postman collection is included in:

```text
postman/Employee API.postman_collection.json
```

The collection contains automated requests and validations for:

- Authentication
- Invalid credentials
- Actuator health
- Employee creation
- Get all employees
- Get employee by ID
- Partial employee search
- Employee update
- Input validation
- Employee not found
- Employee deletion

The collection currently contains **23 automated checks**.

Import the collection into Postman and configure:

```text
baseUrl = http://localhost:8080
username = admin
password = admin123
```

The generated employee ID is stored as a collection variable and reused by subsequent requests.

## Continuous Integration

Continuous Integration is implemented using **GitHub Actions**.

Workflow:

```text
.github/workflows/ci.yml
```

The workflow runs on:

- Pushes to `main`
- Pushes to `develop`
- Pull requests targeting `main`
- Pull requests targeting `develop`

The CI pipeline performs:

```text
Checkout repository
        ↓
Set up Java 17
        ↓
Restore Maven cache
        ↓
mvn -B clean verify
        ↓
Compile + Tests + JaCoCo
```

A failed automated test causes the CI build to fail.

## Git Workflow

The repository uses a Git Flow-inspired branching strategy.

```text
feature/*
    │
    ▼
 develop
    │
    ▼
  main
```

### `main`

Contains stable versions of the application.

### `develop`

Integration branch for completed development work.

### `feature/*`

Used for individual features or changes.

Example:

```bash
git checkout develop
git pull
git checkout -b feature/readme-documentation
```

After completing the change, a Pull Request is created from the feature branch into `develop`.

CI is executed automatically before integration.

## Design Decisions

### Hexagonal Architecture

Hexagonal Architecture separates business logic from infrastructure concerns.

The application layer communicates with external systems through ports, while adapters provide implementations for technologies such as REST and JPA.

This reduces coupling and improves testability and maintainability.

### DTO and Domain Separation

REST DTOs are separated from the domain model.

This prevents HTTP-specific concerns such as validation and JSON date formatting from being introduced into the core domain.

### Persistence Separation

The domain `Employee` model is different from the JPA `EmployeeEntity`.

The application therefore does not depend directly on JPA annotations or Hibernate.

### Repository Port

The application defines `EmployeeRepositoryPort` instead of depending directly on `JpaRepository`.

The JPA adapter implements this port and delegates persistence operations to Spring Data JPA.

### Partial Updates

A dedicated update command uses nullable fields to distinguish between:

```text
field not provided
```

and:

```text
field explicitly provided
```

This is particularly important for Boolean values such as the employee active status.

### Automated Test Database

H2 is used for automated integration tests.

This allows:

```bash
mvn clean verify
```

to run locally and in GitHub Actions without requiring an external MySQL server.

MySQL remains the runtime database.

### Containerization

Docker uses a multi-stage build so Maven and build dependencies are not required in the final runtime image.

Docker Compose provides the API and MySQL environment required to run the complete application.

## Quality Practices

The project applies:

- SOLID principles
- Dependency inversion through ports
- Separation of concerns
- DTO/domain/entity separation
- Centralized exception handling
- Input validation
- Automated unit and integration tests
- Code coverage with JaCoCo
- Structured logging
- API documentation
- Health monitoring
- Environment-based configuration
- Containerization
- Continuous Integration

## Author

Arturo Martínez Sauza