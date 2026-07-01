# API Gateway - QuicklyViajes

## Descripción

El API Gateway es el punto único de entrada (single entry point) para el sistema de microservicios QuicklyViajes. 

### Responsabilidades

1. **Enrutamiento**: Dirige las peticiones hacia el microservicio correspondiente según la ruta.
2. **Validación centralizada de JWT**: Valida el token en CADA petición antes de permitir el acceso.
3. **Inyección de contexto**: Agrega headers con información del usuario (username y rol) para consumo interno.
4. **Descubrimiento dinámico**: Se integra con Eureka para detectar automáticamente instancias disponibles de microservicios.

## Puertos y Configuración

- **Puerto**: 8080
- **Eureka**: http://localhost:8761/eureka/

## Rutas Configuradas

El Gateway enruta hacia los 10 microservicios:

| Ruta         | Microservicio     | Puerto |
|--------------|-------------------|--------|
| `/auth/**`   | ms-auth           | 8091   |
| `/pasajeros/**` | ms-pasajeros   | 8081   |
| `/conductores/**` | ms-conductores | 8082   |
| `/viajes/**` | ms-viajes         | 8083   |
| `/pagos/**`  | ms-pagos          | 8084   |
| `/tarifas/**` | ms-tarifas       | 8085   |
| `/calificaciones/**` | ms-calificaciones | 8086 |
| `/incidencias/**` | ms-incidencias | 8087   |
| `/vehiculos/**` | ms-vehiculos    | 8088   |
| `/zonas/**`  | ms-zonas          | 8089   |
| `/notificaciones/**` | ms-notificaciones | 8090 |

## Flujo de Autenticación y Autorización

```
Cliente
   |
   v
[API Gateway - Puerto 8080]
   |
   +--- ¿Es ruta pública? (/auth/login, /auth/register)
   |    Si → Pasar al microservicio SIN validar JWT
   |    No → Continuar
   |
   +--- ¿Existe header "Authorization: Bearer <token>"?
   |    No → Responder 401 Unauthorized
   |    Si → Continuar
   |
   +--- Validar firma y expiración del JWT usando JwtUtil
   |    No válido → Responder 401 Unauthorized
   |    Válido → Continuar
   |
   +--- Extraer username y rol del token
   |
   +--- Agregar headers al request:
   |    - X-User-Id: <username>
   |    - X-User-Role: <rol>
   |
   +--- Enrutar hacia microservicio correspondiente
   |
   v
[Microservicio de destino]
(Confía en los headers X-User-Id y X-User-Role)
```

## Rutas Públicas (Sin Autenticación)

Las siguientes rutas **no requieren JWT**:

- `POST /auth/login` → Autenticar usuario
- `POST /auth/register` → Registrar nuevo usuario
- `GET /h2-console` → Consola H2 (solo desarrollo)
- `GET /actuator/health` → Health check

Todas las demás rutas requieren un token JWT válido en el header `Authorization: Bearer <token>`.

## Uso del Gateway en Desarrollo Local

### 1. Levantar Eureka Server

```bash
cd eureka-server
./mvnw spring-boot:run
```

Eureka estará disponible en: http://localhost:8761/

### 2. Levantar el API Gateway

```bash
cd api-gateway
./mvnw spring-boot:run
```

El Gateway estará disponible en: http://localhost:8080/

### 3. Levantar los microservicios

Cada microservicio se levanta normalmente:

```bash
cd ms-auth
./mvnw spring-boot:run

cd ms-pasajeros
./mvnw spring-boot:run

# ... etc para los demás microservicios
```

Cada microservicio se registrará automáticamente en Eureka al levantar.

### 4. Probar el flujo completo

#### a. Registrar un usuario (ruta pública)

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "diego",
    "email": "diego@example.com",
    "password": "Password123!"
  }'
```

Respuesta esperada:
```json
{
  "id": 1,
  "username": "diego",
  "email": "diego@example.com",
  "rol": "PASAJERO"
}
```

#### b. Autenticarse y obtener token (ruta pública)

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "diego",
    "password": "Password123!"
  }'
```

Respuesta esperada:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "diego",
  "rol": "PASAJERO"
}
```

#### c. Usar el token para acceder a ruta protegida

```bash
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer <token_obtenido>"
```

Donde `<token_obtenido>` es el valor del campo "token" de la respuesta anterior.

Respuesta esperada:
```json
{
  "id": 1,
  "username": "diego",
  "email": "diego@example.com",
  "rol": "PASAJERO"
}
```

#### d. Verificar que el Gateway agrega los headers

El Gateway ahora agrega headers internos `X-User-Id` y `X-User-Role` que los microservicios pueden usar.
Por ejemplo, en un controlador de ms-pasajeros:

```java
@GetMapping("/perfil")
public ResponseEntity<?> miPerfil(
    @RequestHeader("X-User-Id") String userId,
    @RequestHeader("X-User-Role") String userRole) {
    // userId = "diego"
    // userRole = "PASAJERO"
    return ResponseEntity.ok("Perfil del usuario: " + userId);
}
```

## Configuración de JWT

El Gateway usa la **misma clave JWT** que ms-auth:

```yaml
jwt:
  secret: "QuicklyViajesGrupo11SecretKeyParaFirmarJWT_DEBE_SER_LARGA_2026"
```

⚠️ **IMPORTANTE**: Antes de desplegar a producción, esta clave debe:
1. Moverse a una variable de entorno (`JWT_SECRET`)
2. Reemplazarse por una clave aleatoria de al menos 256 bits (para HS256)

## Componentes Principales

### 1. `ApiGatewayApplication.java`
Clase principal con anotación `@EnableDiscoveryClient` para registro en Eureka.

### 2. `JwtGatewayFilter.java`
Filtro global implementado `GlobalFilter` que:
- Intercepta todas las peticiones
- Valida JWT (excepto en rutas públicas)
- Agrega headers con información del usuario
- Bloquea acceso con respuesta 401 si el token es inválido

### 3. `JwtUtil.java`
Utilidad para validar y extraer información del JWT:
- `isTokenValid(token)` → valida firma y expiración
- `extractUsername(token)` → obtiene el username del token
- `extractRole(token)` → obtiene el rol del token

## Integración con Eureka

El Gateway se registra en Eureka como `api-gateway` y descubre dinámicamente los microservicios.

En `application.yml`:
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
```

Esto permite que el Gateway:
1. Se registre a sí mismo en Eureka
2. Obtenga la lista de microservicios disponibles
3. Detecte cambios automáticamente (si un servicio cae o se reinicia)

## Troubleshooting

### El Gateway no puede conectar a Eureka

**Problema**: `ERR: Unable to register with eureka server`

**Solución**: Asegúrate de que Eureka está levantado en `http://localhost:8761/`

```bash
curl http://localhost:8761/
```

### El Gateway rechaza el token con "Token JWT inválido"

**Problema**: Respuesta 401 Unauthorized

**Causas posibles**:
1. El token fue generado con una clave diferente
2. El token expiró (default: 1 hora)
3. La firma está corrupta

**Solución**: Regenera el token haciendo login nuevamente.

### El Gateway no encuentra el microservicio

**Problema**: `503 Service Unavailable`

**Solución**: Verifica que:
1. El microservicio está levantado
2. El microservicio está registrado en Eureka (http://localhost:8761/)
3. El nombre del servicio en `spring.application.name` es correcto

## Próximas Fases

- **FASE 3**: Documentación Swagger/OpenAPI y HATEOAS
- **FASE 4**: Cambio a MySQL para persistencia de datos
- **FASE 5**: Tests unitarios con JUnit/Mockito/JaCoCo
- **FASE 6**: Docker y docker-compose
- **FASE 7**: Despliegue en Render (CD)
- **FASE 8**: README.md raíz y cierre de proyecto
