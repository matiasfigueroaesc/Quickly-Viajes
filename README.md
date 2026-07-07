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

Abre una terminal en la raíz del proyecto (donde está `docker-compose.yml`) y ejecuta:

```bash
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

## Rutas de referencia

Lo normal es probar todo a través del **API Gateway** (`http://localhost:18080`), que es el único punto de entrada protegido con JWT. Pero cada microservicio también expone su puerto directo por si quieres probarlo aislado con Postman o revisar su Swagger.

| Microservicio | Ruta vía Gateway | Puerto directo | Swagger UI |
|---|---|---|---|
| ms-auth | `/auth/register`, `/auth/login`, `/auth/me` | 8091 | http://localhost:8091/swagger-ui.html |
| ms-pasajeros | `/pasajeros/**` | 8081 | http://localhost:8081/swagger-ui.html |
| ms-conductores | `/conductores/**` | 8082 | http://localhost:8082/swagger-ui.html |
| ms-viajes | `/viajes/**` | 8083 | http://localhost:8083/swagger-ui.html |
| ms-pagos | `/pagos/**` | 8084 | http://localhost:8084/swagger-ui.html |
| ms-tarifas | `/tarifas/**` | 8085 | http://localhost:8085/swagger-ui.html |
| ms-calificaciones | `/calificaciones/**` | 8086 | http://localhost:8086/swagger-ui.html |
| ms-incidencias | `/incidencias/**` | 8087 | http://localhost:8087/swagger-ui.html |
| ms-vehiculos | `/vehiculos/**` | 8088 | http://localhost:8088/swagger-ui.html |
| ms-zonas | `/zonas/**` | 8089 | http://localhost:8089/swagger-ui.html |
| ms-notificaciones | `/notificaciones/**` | 8090 | http://localhost:8090/swagger-ui.html |

Cada ruta soporta las operaciones CRUD típicas (`GET`, `POST`, `PUT`, `DELETE`), salvo `ms-auth` que expone `register`, `login` y `me`.

> Nota: si accedes directo por el puerto del microservicio (sin pasar por el Gateway), no hay validación JWT — es útil para pruebas rápidas, pero el flujo "real" del sistema siempre es a través del Gateway.

## Autenticación

Todas las rutas excepto `POST /auth/login` y `POST /auth/register` requieren el header:

```
Authorization: Bearer <token>
```

El token se obtiene al hacer login en `/auth/login`.

