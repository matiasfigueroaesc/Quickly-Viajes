# ms-auth

Microservicio de autenticación y emisión de JWT para QuicklyViajes (Grupo 11).

## Integrantes del equipo

- [Nombre Integrante 1]
- [Nombre Integrante 2]
- [Nombre Integrante 3]

## Descripción

Microservicio responsable de gestionar usuarios/credenciales, hashear
contraseñas con BCrypt, y emitir tokens JWT al autenticar. A partir de
FASE 2, el API Gateway será el único punto que valide el JWT en cada
request entrante hacia el resto de los microservicios; ms-auth solo
valida su propio token internamente (endpoint `/auth/me`) mientras el
Gateway no existe todavía.

## Tecnologías utilizadas

- Java 21
- Spring Boot 3.2.5
- Spring Security + BCryptPasswordEncoder
- JJWT 0.12.5 (emisión/validación de JWT, firma HS256)
- Spring Data JPA + Hibernate
- Flyway (migraciones de base de datos)
- H2 (archivo local, en `./data/authdb`)

## Endpoints

| Método | Ruta            | Acceso   | Descripción                                  |
|--------|-----------------|----------|-----------------------------------------------|
| POST   | `/auth/register`| Público  | Crea un usuario nuevo (password hasheado)     |
| POST   | `/auth/login`    | Público  | Valida credenciales, devuelve JWT             |
| GET    | `/auth/me`       | JWT      | Devuelve los datos del usuario autenticado    |

## Cómo probar localmente

1. Levantar el microservicio (puerto **8091**):
   ```
   ./mvnw spring-boot:run
   ```

2. Registrar un usuario:
   ```
   POST http://localhost:8091/auth/register
   Content-Type: application/json

   {
     "username": "matias",
     "email": "matias@quicklyviajes.com",
     "password": "Clave123",
     "rol": "PASAJERO"
   }
   ```

3. Iniciar sesión:
   ```
   POST http://localhost:8091/auth/login
   Content-Type: application/json

   {
     "username": "matias",
     "password": "Clave123"
   }
   ```
   Devuelve un `token` JWT en la respuesta.

4. Probar el endpoint protegido con el token obtenido:
   ```
   GET http://localhost:8091/auth/me
   Authorization: Bearer <token>
   ```

5. Usuario sembrado por la migración Flyway para pruebas rápidas:
   - username: `admin`
   - password: `Admin123!`

## Notas de seguridad para fases posteriores

- El secreto JWT (`jwt.secret` en `application.yml`) es un valor de
  **desarrollo**. Antes de desplegar (FASE 6/7) debe moverse a una
  variable de entorno y reemplazarse por un secreto aleatorio largo.
- La validación del JWT para el resto de los microservicios del sistema
  se centraliza en el API Gateway (FASE 2), siguiendo el enfoque
  "Gateway como punto único de validación" acordado en la Instrucción
  Maestra del proyecto.
