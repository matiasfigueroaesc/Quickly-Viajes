# ms-vehiculos

Microservicio de gestión de vehículos desarrollado con Spring Boot 3.2.5 y Java 21.

## Puerto

El servicio corre en el puerto **8083**.

## Tecnologías

- Spring Boot 3.2.5
- Spring Data JPA
- Spring Validation
- H2 (base de datos en memoria)
- Flyway (migraciones)
- Lombok

## Endpoints

| Método | Ruta                   | Descripción              |
|--------|------------------------|--------------------------|
| GET    | /api/vehiculos         | Lista todos los vehículos |
| GET    | /api/vehiculos/{id}    | Obtiene un vehículo por ID |
| POST   | /api/vehiculos         | Crea un nuevo vehículo    |
| PUT    | /api/vehiculos/{id}    | Actualiza un vehículo     |
| DELETE | /api/vehiculos/{id}    | Elimina un vehículo       |

## Ejemplo de body (POST / PUT)

```json
{
  "patente": "ABCD12",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2022,
  "tipo": "Sedan"
}
```

## Consola H2

Disponible en: [http://localhost:8083/h2-console](http://localhost:8083/h2-console)

- JDBC URL: `jdbc:h2:mem:vehiculosdb`
- Usuario: `sa`
- Contraseña: `password`

## Ejecutar

```bash
./mvnw spring-boot:run
```
