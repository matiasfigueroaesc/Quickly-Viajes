# QuicklyViajes

Backend de una plataforma de viajes (tipo ride-hailing) construido con arquitectura de microservicios en Spring Boot, con API Gateway, Eureka y MySQL. Todo el sistema corre en contenedores Docker.

## Integrantes

- Diego Silva
- Diego Gajardo
- Matías Figueroa
- Ignacio Bravo

**Asignatura:** Fullstack 1 — **Docente:** Erwin Aguilera Segura

## Tecnologías

Java 21 · Spring Boot 3.2.5 · Spring Cloud Gateway · Eureka · Spring Data JPA + Flyway · MySQL 8 · JWT · Docker / Docker Compose

## Microservicios

`ms-auth` · `ms-pasajeros` · `ms-conductores` · `ms-viajes` · `ms-pagos` · `ms-tarifas` · `ms-calificaciones` · `ms-incidencias` · `ms-vehiculos` · `ms-zonas` · `ms-notificaciones`

Más `eureka-server` (registro de servicios) y `api-gateway` (puerta de entrada única + validación JWT).

## Cómo levantar el proyecto

Requisitos: Docker y Docker Compose.

```bash
git clone <URL_DEL_REPOSITORIO>
cd "proyecto evaluacion 3 terminado)"
docker compose up --build
```

Esto levanta MySQL (con las 11 bases de datos ya creadas), Eureka, el Gateway y los 11 microservicios.

- Eureka Dashboard: http://localhost:18761
- API Gateway (entrada única para Postman): http://localhost:18080

Para bajar todo:

```bash
docker compose down
```

Para bajar todo y borrar los datos de MySQL:

```bash
docker compose down -v
```

## Rutas principales (vía API Gateway)

Todas las peticiones se hacen contra `http://localhost:18080`, no directo a los microservicios.

| Ruta | Microservicio |
|---|---|
| `/auth/register`, `/auth/login`, `/auth/me` | ms-auth |
| `/pasajeros/**` | ms-pasajeros |
| `/conductores/**` | ms-conductores |
| `/viajes/**` | ms-viajes |
| `/pagos/**` | ms-pagos |
| `/tarifas/**` | ms-tarifas |
| `/calificaciones/**` | ms-calificaciones |
| `/incidencias/**` | ms-incidencias |
| `/vehiculos/**` | ms-vehiculos |
| `/zonas/**` | ms-zonas |
| `/notificaciones/**` | ms-notificaciones |

Cada ruta soporta las operaciones CRUD típicas (`GET`, `POST`, `PUT`, `DELETE`), salvo `ms-auth` que expone `register`, `login` y `me`.

## Autenticación

Todas las rutas excepto `POST /auth/login` y `POST /auth/register` requieren el header:

```
Authorization: Bearer <token>
```

El token se obtiene al hacer login en `/auth/login`.
