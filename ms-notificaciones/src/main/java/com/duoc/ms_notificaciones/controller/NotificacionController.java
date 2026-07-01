package com.duoc.ms_notificaciones.controller;

import com.duoc.ms_notificaciones.model.dto.NotificacionDTO;
import com.duoc.ms_notificaciones.model.entity.Notificacion;
import com.duoc.ms_notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "notificaciones", description = "Gestión de notificaciones")
@SecurityRequirement(name = "Bearer Authentication")
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);

    private final NotificacionService service;

    // GET /api/notificaciones → lista todas
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        log.info("GET /api/notificaciones - solicitando lista de notificaciones");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/notificaciones/{id} → busca una por ID
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/notificaciones/{} - buscando notificación", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/notificaciones/viaje/{viajeId} → busca por viaje
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Notificacion>> obtenerPorViaje(@PathVariable Long viajeId) {
        log.info("GET /api/notificaciones/viaje/{} - buscando notificaciones por viaje", viajeId);
        return ResponseEntity.ok(service.obtenerPorViajeId(viajeId));
    }

    // GET /api/notificaciones/estado/{estado} → filtra por estado
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/notificaciones/estado/{} - buscando por estado", estado);
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // POST /api/notificaciones → envía una nueva notificación
    @Operation(summary = "Post operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Notificacion> enviar(@Valid @RequestBody NotificacionDTO dto) {
        log.info("POST /api/notificaciones - enviando nueva notificación");
        Notificacion nueva = service.enviar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/notificaciones/{id} → actualiza una existente
    @Operation(summary = "Put operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody NotificacionDTO dto) {
        log.info("PUT /api/notificaciones/{} - actualizando notificación", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/notificaciones/{id} → elimina una
    @Operation(summary = "Delete operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{} - eliminando notificación", id);
        service.eliminar(id);
        return ResponseEntity.ok("Notificación con id " + id + " eliminada correctamente");
    }

}
