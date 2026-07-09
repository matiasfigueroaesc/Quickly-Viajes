package com.duoc.ms_vehiculos.controller;

import com.duoc.ms_vehiculos.model.dto.VehiculoDTO;
import com.duoc.ms_vehiculos.model.entity.Vehiculo;
import com.duoc.ms_vehiculos.service.VehiculoService;
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
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
@Tag(name = "vehiculos", description = "Gestión de vehiculos")
@SecurityRequirement(name = "Bearer Authentication")
public class VehiculoController {

    private static final Logger log = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    // GET /api/vehiculos → lista todos
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        log.info("GET /api/vehiculos - solicitando lista de vehículos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/vehiculos/{id} → busca uno por ID
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/vehiculos/{} - buscando vehículo", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/vehiculos → crea uno nuevo
    @Operation(summary = "Post operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoDTO dto) {
        log.info("POST /api/vehiculos - creando nuevo vehículo");
        Vehiculo nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/vehiculos/{id} → actualiza uno existente
    @Operation(summary = "Put operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody VehiculoDTO dto) {
        log.info("PUT /api/vehiculos/{} - actualizando vehículo", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/vehiculos/{id} → elimina uno
    @Operation(summary = "Delete operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/vehiculos/{} - eliminando vehículo", id);
        service.eliminar(id);
        return ResponseEntity.ok("Vehículo con id " + id + " eliminado correctamente");
    }

}
