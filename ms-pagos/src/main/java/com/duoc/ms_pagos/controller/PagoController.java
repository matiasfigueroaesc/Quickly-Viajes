package com.duoc.ms_pagos.controller;

import com.duoc.ms_pagos.model.dto.PagoDTO;
import com.duoc.ms_pagos.model.entity.Pago;
import com.duoc.ms_pagos.service.PagoService;
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
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "pagos", description = "Gestión de pagos")
@SecurityRequirement(name = "Bearer Authentication")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    private final PagoService service;

    // GET /api/pagos → lista todos los pagos
    @Operation(summary = "Obtener pagos", description = "Busca y retorna un registro de pagos")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping
    public ResponseEntity<List<Pago>> listar() {
        log.info("GET /api/pagos - solicitando lista de pagos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/pagos/{id} → busca pago por ID
    @Operation(summary = "Obtener pagos", description = "Busca y retorna un registro de pagos")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pagos/{} - buscando pago", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/pagos/viaje/{viajeId} → todos los pagos de un viaje específico
    @Operation(summary = "Obtener pagos", description = "Busca y retorna un registro de pagos")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Pago>> obtenerPorViaje(@PathVariable Long viajeId) {
        log.info("GET /api/pagos/viaje/{} - buscando pagos del viaje", viajeId);
        return ResponseEntity.ok(service.obtenerPorViaje(viajeId));
    }

    // GET /api/pagos/estado/{estado} → filtra pagos por estado
    @Operation(summary = "Obtener pagos", description = "Busca y retorna un registro de pagos")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/pagos/estado/{} - filtrando pagos por estado", estado);
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // POST /api/pagos → registra un nuevo pago
    @Operation(summary = "Crear nuevo pagos", description = "Crea un nuevo registro de pagos en el sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        log.info("POST /api/pagos - registrando nuevo pago para viaje id: {}", dto.getViajeId());
        Pago nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/pagos/{id} → actualiza un pago existente
    @Operation(summary = "Actualizar pagos", description = "Actualiza un registro de pagos existente")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id,
                                            @Valid @RequestBody PagoDTO dto) {
        log.info("PUT /api/pagos/{} - actualizando pago", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/pagos/{id} → elimina un pago
    @Operation(summary = "Eliminar pagos", description = "Elimina un registro de pagos del sistema")

@ApiResponses({

    @ApiResponse(responseCode = "200", description = "Success"),

    @ApiResponse(responseCode = "401", description = "Unauthorized")

})

@DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{} - eliminando pago", id);
        service.eliminar(id);
        return ResponseEntity.ok("Pago con id " + id + " eliminado correctamente");
    }
}
