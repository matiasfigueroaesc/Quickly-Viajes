```
                                                                                           
  ▄▄▄▄           ▀           █      ▀▀█           ▄    ▄   ▀              ▀                
 ▄▀  ▀▄ ▄   ▄  ▄▄▄     ▄▄▄   █   ▄    █    ▄   ▄  ▀▄  ▄▀ ▄▄▄     ▄▄▄    ▄▄▄    ▄▄▄    ▄▄▄  
 █    █ █   █    █    █▀  ▀  █ ▄▀     █    ▀▄ ▄▀   █  █    █    ▀   █     █   █▀  █  █   ▀ 
 █    █ █   █    █    █      █▀█      █     █▄█    ▀▄▄▀    █    ▄▀▀▀█     █   █▀▀▀▀   ▀▀▀▄ 
  █▄▄█▀ ▀▄▄▀█  ▄▄█▄▄  ▀█▄▄▀  █  ▀▄    ▀▄▄   ▀█      ██   ▄▄█▄▄  ▀▄▄▀█     █   ▀█▄▄▀  ▀▄▄▄▀ 
     █                                      ▄▀                            █                
                                           ▀▀                           ▀▀                 
```
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

> **Despliegue:** este proyecto se ejecuta de forma local mediante Docker Compose. Según la aclaración del docente sobre la Evaluación Final Transversal, el despliegue remoto (Render/Railway u otro) es opcional; el equipo optó por entrega 100% local.

## Rutas de referencia

Todas las peticiones se hacen a través del **API Gateway**, único punto de entrada protegido con JWT:

**Base URL:** `http://localhost:18080`

| Microservicio | Ruta vía Gateway | Puerto interno |
|---|---|---|
| ms-auth | `/auth/register`, `/auth/login`, `/auth/me` | 8091 |
| ms-pasajeros | `/pasajeros/**` | 8081 |
| ms-conductores | `/conductores/**` | 8082 |
| ms-viajes | `/viajes/**` | 8083 |
| ms-pagos | `/pagos/**` | 8084 |
| ms-tarifas | `/tarifas/**` | 8085 |
| ms-calificaciones | `/calificaciones/**` | 8086 |
| ms-incidencias | `/incidencias/**` | 8087 |
| ms-vehiculos | `/vehiculos/**` | 8088 |
| ms-zonas | `/zonas/**` | 8089 |
| ms-notificaciones | `/notificaciones/**` | 8090 |

Cada ruta soporta las operaciones CRUD típicas (`GET`, `POST`, `PUT`, `DELETE`), salvo `ms-auth` que expone `register`, `login` y `me`.

## Documentación Swagger

Una vez levantados los contenedores, Swagger queda accesible directamente desde el navegador:

- Gateway: http://localhost:18080/swagger-ui.html
- Auth: http://localhost:8091/swagger-ui.html
- Pasajeros: http://localhost:8081/swagger-ui.html
- Conductores: http://localhost:8082/swagger-ui.html
- Viajes: http://localhost:8083/swagger-ui.html
- Pagos: http://localhost:8084/swagger-ui.html
- Tarifas: http://localhost:8085/swagger-ui.html
- Calificaciones: http://localhost:8086/swagger-ui.html
- Incidencias: http://localhost:8087/swagger-ui.html
- Vehículos: http://localhost:8088/swagger-ui.html
- Zonas: http://localhost:8089/swagger-ui.html
- Notificaciones: http://localhost:8090/swagger-ui.html

Para probar endpoints protegidos, primero inicia sesión en:

```
http://localhost:18080/auth/login
```

con un usuario registrado (por ejemplo `admin` / `Admin123!`) y copia el token JWT en el botón Authorize de Swagger.

## Autenticación

Todas las rutas excepto `POST /auth/login` y `POST /auth/register` requieren el header:

```
Authorization: Bearer <token>
```

El token se obtiene al hacer login en `/auth/login`.
