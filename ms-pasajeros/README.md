# ms-pasajeros

Microservicio de gestión de pasajeros desarrollado con Spring Boot.

## Integrantes del equipo

- [Nombre Integrante 1]
- [Nombre Integrante 2]
- [Nombre Integrante 3]

## Descripción

Microservicio REST encargado de administrar los datos de pasajeros dentro de la arquitectura de microservicios del proyecto semestral. Permite registrar, consultar, actualizar y eliminar pasajeros, aplicando validaciones de negocio y persistencia real con JPA + H2.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.2.5
- Spring Data JPA + Hibernate
- Flyway (migraciones de base de datos)
- H2 (base de datos en memoria)
- Bean Validation (JSR 380)
- Lombok
- SLF4J (logs)

## Estructura del proyecto

```
ms-pasajeros/
├── controller/        → PasajeroController (endpoints REST)
├── service/           → PasajeroService (lógica de negocio)
├── repository/        → PasajeroRepository (acceso a datos)
├── model/
│   ├── entity/        → Pasajero (entidad JPA)
│   └── dto/           → PasajeroDTO (datos de entrada + validaciones)
├── exception/         → GlobalExceptionHandler, ErrorResponse, PasajeroNotFoundException
└── resources/
    ├── application.yml
    └── db/migration/  → V1__crear_tabla_pasajeros.sql
```

## Funcionalidades implementadas

- Listar todos los pasajeros (`GET /api/pasajeros`)
- Buscar pasajero por ID (`GET /api/pasajeros/{id}`)
- Crear nuevo pasajero (`POST /api/pasajeros`)
- Actualizar pasajero existente (`PUT /api/pasajeros/{id}`)
- Eliminar pasajero (`DELETE /api/pasajeros/{id}`)

## Reglas de negocio

- No se permite registrar dos pasajeros con el mismo email (validación en capa Service).
- El nombre debe tener entre 2 y 100 caracteres.
- El teléfono debe seguir el formato internacional (9 a 15 dígitos).

## Pasos para ejecutar

**Requisitos previos:**
- Java 21 instalado
- Maven 3.x instalado (o usar el wrapper `./mvnw`)

**Ejecutar la aplicación:**
```bash
./mvnw spring-boot:run
```

La aplicación inicia en `http://localhost:8081`

**Consola H2 (base de datos):**
```
http://localhost:8081/h2-console
JDBC URL: jdbc:h2:mem:pasajerosdb
Usuario: sa
Contraseña: password
```

## Ejemplos de uso con Postman

**Crear pasajero:**
```
POST http://localhost:8081/api/pasajeros
Content-Type: application/json

{
  "nombre": "Ana Torres",
  "email": "ana@correo.cl",
  "telefono": "+56912345678"
}
```

**Obtener todos:**
```
GET http://localhost:8081/api/pasajeros
```

**Buscar por ID:**
```
GET http://localhost:8081/api/pasajeros/1
```

**Actualizar:**
```
PUT http://localhost:8081/api/pasajeros/1
Content-Type: application/json

{
  "nombre": "Ana Torres Soto",
  "email": "ana.nueva@correo.cl",
  "telefono": "+56998765432"
}
```

**Eliminar:**
```
DELETE http://localhost:8081/api/pasajeros/1
```

## Puerto

Este microservicio corre en el puerto `8081`.
