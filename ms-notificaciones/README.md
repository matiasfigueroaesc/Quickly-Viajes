# ms-notificaciones

Microservicio de gestión de notificaciones desarrollado con Spring Boot 3.2.5 y Java 21.

## Puerto

El servicio corre en el puerto **8090**.

## Dependencias

Depende de **ms-viajes** (puerto 8088) para obtener el `viajeId` asociado a cada notificación.

## Tecnologías

- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- H2 (base de datos en memoria)
- Flyway (migraciones)
- Lombok

## Endpoints

| Método | Ruta                                    | Descripción                              |
|--------|-----------------------------------------|------------------------------------------|
| GET    | /api/notificaciones                     | Lista todas las notificaciones            |
| GET    | /api/notificaciones/{id}                | Obtiene una notificación por ID           |
| GET    | /api/notificaciones/viaje/{viajeId}     | Lista notificaciones de un viaje          |
| GET    | /api/notificaciones/estado/{estado}     | Filtra por estado (PENDIENTE/ENVIADA/FALLIDA) |
| POST   | /api/notificaciones                     | Envía una nueva notificación              |
| PUT    | /api/notificaciones/{id}                | Actualiza una notificación                |
| DELETE | /api/notificaciones/{id}                | Elimina una notificación                  |

## Valores permitidos

**tipo:** `INICIO_VIAJE`, `FIN_VIAJE`, `CANCELACION`, `DEMORA`, `RECORDATORIO`

**canal:** `EMAIL`, `SMS`, `PUSH`

**estado:** `PENDIENTE`, `ENVIADA`, `FALLIDA`

## Ejemplo de body (POST / PUT)

```json
{
  "viajeId": 1,
  "tipo": "INICIO_VIAJE",
  "mensaje": "Tu viaje ha comenzado. ¡Buen viaje!",
  "canal": "EMAIL"
}
```

## Consola H2

Disponible en: [http://localhost:8090/h2-console](http://localhost:8090/h2-console)

- JDBC URL: `jdbc:h2:mem:notificacionesdb`
- Usuario: `sa`
- Contraseña: `password`

## Ejecutar

```bash
./mvnw spring-boot:run
```
