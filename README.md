# OsenShop

OsenShop is an e-commerce application built with Java 21 and Spring Boot for the backend, focused on clean architecture, security best practices, and portfolio-oriented design.

The project implements JWT-based authentication, DTO handling, custom exceptions, Spring Security filters, and pagination. It combines Clean Architecture for the authentication module with a feature-based architecture for the rest of the domain.
The project also follows SOLID principles.

The frontend repository that consumes this backend can be found here:
https://github.com/osenpb/f_osenshop

## Main Technologies

- Java 21
- Spring Boot
- Spring Security + JWT + Nimbus JOSE
- Spring Data JPA
- Hibernate
- REST API
- Maven
- Database: PostgreSQL
- Docker

## Security and Authentication

The authentication module follows Clean Architecture principles, clearly separating:

- **Domain**: Core business rules and models.
- **Application**: Use cases (login, registration, validation).
- **Infrastructure**: JWT implementation, repositories, and security configuration.
- **Entrypoints**: REST controllers.

## Features

- JWT-based authentication and authorization.
- Custom Spring Security filters.
- Clear separation of concerns across layers.
- Centralized security error handling.
- Refresh token rotation.

## Project Architecture

### Hybrid approach

- **Auth module** → Clean Architecture.
- **Remaining domain modules** → Feature-based architecture.

This approach provides:

- Maximum clarity for a critical module such as authentication.
- Scalability and functional organization across the rest of the system.

The project uses DTOs to:

- Prevent direct exposure of entities.
- Control the API contract.
- Facilitate validation and model evolution.

Includes:

- Request and response DTOs.
- Explicit mapping between entities and DTOs.

### Exception Handling

- Domain-specific custom exceptions.
- Global exception handling using `@ControllerAdvice`.
- Clear and consistent error responses.

### Pagination

- The API implements pagination using Spring’s `Pageable`.
- Support for `page`, `size`, and `sort` parameters.
- Optimized responses for large datasets.
- Compatible with modern frontend applications.

## Project Focus

OsenShop is designed as:

- A professional backend showcase project.
- A solid foundation for a real-world e-commerce system.
- An example of modern Spring Boot best practices.

Special emphasis on:

- Clean architecture
- Security
- Scalability
- Code readability

## Upcoming Improvements

- Unit and integration testing.
- API documentation with OpenAPI / Swagger.
