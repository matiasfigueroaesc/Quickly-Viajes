package com.duoc.ms_conductores.controller;

import com.duoc.ms_conductores.model.dto.ConductorDTO;
import com.duoc.ms_conductores.model.entity.Conductor;
import com.duoc.ms_conductores.service.ConductorService;
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
@RequestMapping("/api/conductores")
@RequiredArgsConstructor
@Tag(name = "conductores", description = "Gestión de conductores")
@SecurityRequirement(name = "Bearer Authentication")
public class ConductorController {

    private static final Logger log = LoggerFactory.getLogger(ConductorController.class);

    private final ConductorService service;

    // GET /api/conductores → lista todos
    @Operation(summary = "Obtener conductores", description = "Busca y retorna un registro de conductores")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Conductor>> listar() {
        log.info("GET /api/conductores - solicitando lista de conductores");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/conductores/{id} → busca uno por ID
    @Operation(summary = "Obtener conductores", description = "Busca y retorna un registro de conductores")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Conductor> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/conductores/{} - buscando conductor", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/conductores → crea uno nuevo
    @Operation(summary = "Crear nuevo conductores", description = "Crea un nuevo registro de conductores en el sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Conductor> crear(@Valid @RequestBody ConductorDTO dto) {
        log.info("POST /api/conductores - creando nuevo conductor");
        Conductor nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/conductores/{id} → actualiza uno existente
    @Operation(summary = "Actualizar conductores", description = "Actualiza un registro de conductores existente")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Conductor> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody ConductorDTO dto) {
        log.info("PUT /api/conductores/{} - actualizando conductor", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/conductores/{id} → elimina uno
    @Operation(summary = "Eliminar conductores", description = "Elimina un registro de conductores del sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/conductores/{} - eliminando conductor", id);
        service.eliminar(id);
        return ResponseEntity.ok("Conductor con id " + id + " eliminado correctamente");
    }

}
