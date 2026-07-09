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

Una vez levantados los contenedores, Swagger queda accesible directamente desde el navegador para acceder a las operaciones sin necesidad de memorizar las rutas:

- Link: http://localhost:18080/swagger-ui.html

Puedes seleccionar a qué microservicio acceder seleccionando en el menú superior "Select a definition".

## Autenticación

Todas las rutas excepto `POST /auth/login` y `POST /auth/register` requieren inicio de sesión e ingreso de token.

Para probar endpoints protegidos, primero debes iniciar sesion en el auth con un usuario registrado y copia el token JWT en el botón Authorize de Swagger cuando sea necesario.

Contenido del JSON con usuario y contraseña funcional:

```bash
{
  "username": "admin",
  "password": "Admin123!"
}
```
 
En caso de usar Postman el token se obtiene al hacer login con los mismos datos en `http://localhost:18080/auth/login`.

Luego se debe ingresar el token en la pestaña `Authorization > Bearer Token > Token` Una vez registrado se puede acceder a los microservicios para realizar las consultas a través de sus respectivas rutas indicadas en la tabla.
