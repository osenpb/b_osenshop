# OsenShop

OsenShop es una aplicación e‑commerce backend desarrollada con Java 21 y Spring Boot, enfocada en buenas prácticas de arquitectura, seguridad y diseño orientado a portafolio profesional.

El proyecto implementa autenticación basada en JWT, manejo de DTOs, excepciones personalizadas, filtros de Spring Security y paginación, combinando Clean Architecture para el módulo de autenticación y una arquitectura feature‑based para el resto del dominio.
Además cumple con principios SOLID.

El repositorio frontend que consume este proyecto se encuentra en: https://github.com/osenpb/f_osenshop

## Tecnologías principales

- Java 21
- Spring Boot
- Spring Security + JWT + Nimbus JOSE
- Spring Data JPA
- Hibernate
- REST API
- Maven
- Base de datos: PostgreSQL
- Docker

## Seguridad y Autenticación

El módulo de autenticación sigue principios de Clean Architecture, separando claramente:

- Dominio: reglas de negocio y modelos centrales.
- Aplicación: casos de uso (login, registro, validación).
- Infraestructura: implementación de JWT, repositorios y seguridad.
- Entrypoints: controladores REST.

## Características

- Autenticación y autorización mediante JWT.
- Filtros personalizados de Spring Security.
- Separación de responsabilidades entre capas.
- Manejo centralizado de errores de seguridad.
- Rotación de refresh tokens.

## Arquitectura del proyecto

Enfoque híbrido

- Auth → Clean Architecture.
- Resto del dominio → Arquitectura feature‑based.

Esto permite:

- Máxima claridad en un módulo crítico como autenticación
- Escalabilidad y organización por contexto funcional en el resto del sistema

El proyecto utiliza DTOs para:

- Evitar exponer entidades directamente
- Controlar el contrato de la API
- Facilitar validaciones y evolución del modelo

Incluye:

- DTOs de request y response
- Conversión explícita entre entidades y DTOs

### Manejo de Excepciones

- Excepciones personalizadas por dominio
- @ControllerAdvice para manejo global
- Respuestas de error claras y consistentes


### Paginación

- La API implementa paginación usando la clase Pageable de Spring.
- Soporte para page, size y sort.
- Respuestas optimizadas para listados grandes.
- Compatible con frontend moderno.

## Enfoque del proyecto

OsenShop está diseñado como:

- Proyecto demostrativo de backend profesional.
- Base sólida para un e‑commerce real.
- Buenas prácticas en Spring Boot moderno.

Especial énfasis en:

- Arquitectura limpia
- Seguridad
- Escalabilidad
- Legibilidad del código

## Próximas mejoras

- Tests unitarios y de integración.
- Documentación con OpenAPI / Swagger.
- Integración con Stripe.
