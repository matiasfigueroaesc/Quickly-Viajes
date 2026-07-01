# Eureka Server - QuicklyViajes

## Descripción

Eureka Server es el servidor central de descubrimiento de servicios (Service Discovery) del sistema QuicklyViajes. Permite que los microservicios se registren automáticamente y que otros servicios los descubran de forma dinámica.

## Puertos y Configuración

- **Puerto**: 8761
- **Hostname**: localhost

## ¿Por qué Eureka?

Sin Eureka, cada microservicio tendría que conocer la IP y puerto exacto de todos los demás servicios. Esto causa:
- Cambios manuales si un servicio cambia de puerto
- Problemas si un servicio falla y se levanta en otra máquina
- Escalabilidad limitada

Con Eureka:
- Cada microservicio se registra automáticamente
- El API Gateway descubre dinámicamente los servicios disponibles
- Si un servicio cae, Eureka lo detecta y lo elimina del registro
- Si se levantan múltiples instancias, Eureka las balancea

## Levantar Eureka Server

```bash
cd eureka-server
./mvnw spring-boot:run
```

Una vez levantado, accede a:
- **Dashboard**: http://localhost:8761/
- **API REST**: http://localhost:8761/eureka/

## Flujo de Registro

```
1. Eureka Server inicia en puerto 8761
   ↓
2. ms-auth inicia y se registra en Eureka:
   POST http://localhost:8761/eureka/apps/MS-AUTH
   ↓
3. API Gateway inicia y:
   - Se registra en Eureka
   - Obtiene lista de todos los microservicios
   ↓
4. Gateway usa la información de Eureka para hacer load-balancing
   hacia los microservicios (ej: lb://ms-auth)
```

## Dashboard de Eureka

Al acceder a http://localhost:8761/ verás:

- **Instances currently registered with Eureka**: Lista de todos los servicios registrados
- **General Info**: Información del servidor Eureka
- **Instance Info**: Detalles de la instancia de Eureka

Ejemplo de servicios registrados:

```
Instances currently registered with Eureka
-----------
Instance ID                                  Application  Status
ms-pasajeros:8081                           MS-PASAJEROS  UP
ms-conductores:8082                         MS-CONDUCTORES UP
ms-viajes:8083                              MS-VIAJES      UP
ms-pagos:8084                               MS-PAGOS       UP
ms-tarifas:8085                             MS-TARIFAS     UP
ms-calificaciones:8086                      MS-CALIFICACIONES UP
ms-incidencias:8087                         MS-INCIDENCIAS UP
ms-vehiculos:8088                           MS-VEHICULOS   UP
ms-zonas:8089                               MS-ZONAS       UP
ms-notificaciones:8090                      MS-NOTIFICACIONES UP
ms-auth:8091                                MS-AUTH        UP
api-gateway:8080                            API-GATEWAY    UP
```

## Configuración de Eureka Server

El archivo `application.yml` configura:

```yaml
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false    # El servidor NO se registra a sí mismo
    fetchRegistry: false         # El servidor NO necesita obtener registro
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  server:
    enable-self-preservation: false  # Deshabilita protección para desarrollo
    eviction-interval-timer-in-ms: 3000  # Revisa microservicios cada 3s
```

## Configuración de Eureka Client (en los microservicios)

Cada microservicio debe tener en su `application.yml`:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
    register-with-eureka: true   # Se registra en Eureka
    fetch-registry: true         # Obtiene el registro de otros servicios
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}
    prefer-ip-address: false
```

Todos los microservicios ya tienen esta configuración agregada en FASE 2.

## Ciclo de Vida de un Microservicio

### 1. Startup
```
ms-pasajeros inicia
   ↓
Lee configuración Eureka
   ↓
Envía heartbeat a Eureka: "Estoy vivo, soy ms-pasajeros en puerto 8081"
   ↓
Eureka registra: ms-pasajeros → UP
   ↓
Otros servicios pueden descubrirlo inmediatamente
```

### 2. Operación Normal
```
Cada 30 segundos:
ms-pasajeros envía heartbeat a Eureka
   ↓
Eureka actualiza timestamp: "último visto: ahora"
```

### 3. Fallo o Shutdown
```
Si ms-pasajeros NO envía heartbeat por 90 segundos:
   ↓
Eureka marca: ms-pasajeros → DOWN
   ↓
API Gateway deja de rutear hacia ese servicio
   ↓
Si hay más instancias, usa otras
```

## Verificar Servicios Registrados

### Vía Dashboard
http://localhost:8761/

### Vía API REST

Obtener lista de todos los servicios:
```bash
curl http://localhost:8761/eureka/apps
```

Obtener servicios de una aplicación específica:
```bash
curl http://localhost:8761/eureka/apps/MS-PASAJEROS
```

Obtener instancia específica:
```bash
curl http://localhost:8761/eureka/apps/MS-PASAJEROS/ms-pasajeros:8081
```

## Troubleshooting

### Eureka Server no inicia

**Problema**: `Address already in use: 8761`

**Solución**: Otro proceso está usando el puerto 8761. Cambia el puerto en `application.yml` o mata el proceso existente.

```bash
lsof -i :8761  # Encuentra qué proceso lo usa
kill -9 <PID>
```

### Microservicio no se registra en Eureka

**Problema**: El dashboard no muestra el servicio

**Causas posibles**:
1. Eureka Server no está corriendo
2. La URL de Eureka es incorrecta en el microservicio
3. `register-with-eureka: false` en el microservicio

**Solución**:
1. Verifica que Eureka está en http://localhost:8761/
2. Revisa el `application.yml` del microservicio:
   ```yaml
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/
       register-with-eureka: true  # Debe ser true
   ```

### Gateway no puede encontrar el microservicio

**Problema**: Gateway retorna 503 Service Unavailable

**Solución**: 
1. Verifica en Eureka que el microservicio está registrado (UP)
2. Verifica que el nombre en la ruta del Gateway coincide con `spring.application.name`

Ejemplo:
- Microservicio: `spring.application.name: ms-pasajeros`
- Ruta Gateway: `uri: lb://ms-pasajeros` ✓

## Self-Preservation Mode

En desarrollo, hemos deshabilitado el "Self-Preservation Mode" de Eureka. Esto significa:
- Si un microservicio NO envía heartbeat, Eureka lo marca como DOWN inmediatamente
- En producción, este modo debería estar HABILITADO para evitar falsos positivos

Para producción, en `eureka-server/application.yml`:
```yaml
eureka:
  server:
    enable-self-preservation: true  # Habilitar en producción
```

## Próximas Fases

- **FASE 3**: Documentación Swagger/OpenAPI
- **FASE 4**: Cambio a MySQL
- **FASE 5**: Tests unitarios
- **FASE 6**: Docker y docker-compose
- **FASE 7**: Despliegue en Render
- **FASE 8**: README.md raíz y cierre
