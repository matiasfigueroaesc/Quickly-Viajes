package com.duoc.ms_zonas.controller;

import com.duoc.ms_zonas.model.dto.ZonaDTO;
import com.duoc.ms_zonas.model.entity.Zona;
import com.duoc.ms_zonas.service.ZonaService;
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
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
@Tag(name = "zonas", description = "Gestión de zonas")
@SecurityRequirement(name = "Bearer Authentication")
public class ZonaController {

    private static final Logger log = LoggerFactory.getLogger(ZonaController.class);

    private final ZonaService service;

    // GET /api/zonas → lista todas las zonas
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Zona>> listar() {
        log.info("GET /api/zonas - solicitando lista de zonas");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/zonas/activas → lista solo las zonas activas
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/activas")
    public ResponseEntity<List<Zona>> listarActivas() {
        log.info("GET /api/zonas/activas - solicitando zonas activas");
        return ResponseEntity.ok(service.obtenerActivas());
    }

    // GET /api/zonas/{id} → busca una zona por ID
    @Operation(summary = "Get operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Zona> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/zonas/{} - buscando zona", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/zonas → crea una nueva zona
    @Operation(summary = "Post operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Zona> crear(@Valid @RequestBody ZonaDTO dto) {
        log.info("POST /api/zonas - creando nueva zona");
        Zona nueva = service.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/zonas/{id} → actualiza una zona existente
    @Operation(summary = "Put operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Zona> actualizar(@PathVariable Long id,
                                           @Valid @RequestBody ZonaDTO dto) {
        log.info("PUT /api/zonas/{} - actualizando zona", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/zonas/{id} → elimina una zona
    @Operation(summary = "Delete operation", description = "Operation on resource")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/zonas/{} - eliminando zona", id);
        service.eliminar(id);
        return ResponseEntity.ok("Zona con id " + id + " eliminada correctamente");
    }
}
