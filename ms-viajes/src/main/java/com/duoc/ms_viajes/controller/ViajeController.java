package com.duoc.ms_viajes.controller;

import com.duoc.ms_viajes.model.dto.ViajeDTO;
import com.duoc.ms_viajes.model.dto.ViajeDetalleDTO;
import com.duoc.ms_viajes.model.entity.Viaje;
import com.duoc.ms_viajes.service.ViajeService;
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
@RequestMapping("/api/viajes")
@RequiredArgsConstructor
@Tag(name = "viajes", description = "Gestión de viajes")
@SecurityRequirement(name = "Bearer Authentication")
public class ViajeController {

    private static final Logger log = LoggerFactory.getLogger(ViajeController.class);

    private final ViajeService service;

    // GET /api/viajes → lista todos los viajes
    @Operation(summary = "Obtener viajes", description = "Busca y retorna un registro de viajes")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Viaje>> listar() {
        log.info("GET /api/viajes - solicitando lista de viajes");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/viajes/{id} → busca viaje por ID (datos básicos)
    @Operation(summary = "Obtener viajes", description = "Busca y retorna un registro de viajes")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Viaje> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/viajes/{} - buscando viaje", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    /**
     * GET /api/viajes/{id}/detalle → retorna el viaje enriquecido con datos
     * de pasajero, conductor y tarifa consultados desde sus microservicios.
     */
    @Operation(summary = "Obtener viajes", description = "Busca y retorna un registro de viajes")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}/detalle")
    public ResponseEntity<ViajeDetalleDTO> obtenerDetalle(@PathVariable Long id) {
        log.info("GET /api/viajes/{}/detalle - solicitando detalle completo del viaje", id);
        return ResponseEntity.ok(service.obtenerDetallePorId(id));
    }

    // GET /api/viajes/pasajero/{pasajeroId} → historial de viajes de un pasajero
    @Operation(summary = "Obtener viajes", description = "Busca y retorna un registro de viajes")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<Viaje>> obtenerPorPasajero(@PathVariable Long pasajeroId) {
        log.info("GET /api/viajes/pasajero/{} - buscando viajes del pasajero", pasajeroId);
        return ResponseEntity.ok(service.obtenerPorPasajero(pasajeroId));
    }

    // GET /api/viajes/conductor/{conductorId} → historial de viajes de un conductor
    @Operation(summary = "Obtener viajes", description = "Busca y retorna un registro de viajes")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/conductor/{conductorId}")
    public ResponseEntity<List<Viaje>> obtenerPorConductor(@PathVariable Long conductorId) {
        log.info("GET /api/viajes/conductor/{} - buscando viajes del conductor", conductorId);
        return ResponseEntity.ok(service.obtenerPorConductor(conductorId));
    }

    // POST /api/viajes → crea un nuevo viaje
    @Operation(summary = "Crear nuevo viajes", description = "Crea un nuevo registro de viajes en el sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Viaje> crear(@Valid @RequestBody ViajeDTO dto) {
        log.info("POST /api/viajes - creando nuevo viaje");
        Viaje nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/viajes/{id} → actualiza un viaje existente
    @Operation(summary = "Actualizar viajes", description = "Actualiza un registro de viajes existente")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Viaje> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody ViajeDTO dto) {
        log.info("PUT /api/viajes/{} - actualizando viaje", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/viajes/{id} → elimina un viaje
    @Operation(summary = "Eliminar viajes", description = "Elimina un registro de viajes del sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/viajes/{} - eliminando viaje", id);
        service.eliminar(id);
        return ResponseEntity.ok("Viaje con id " + id + " eliminado correctamente");
    }
}
