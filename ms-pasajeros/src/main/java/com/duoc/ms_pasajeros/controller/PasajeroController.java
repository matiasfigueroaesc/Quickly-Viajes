package com.duoc.ms_pasajeros.controller;

import com.duoc.ms_pasajeros.model.dto.PasajeroDTO;
import com.duoc.ms_pasajeros.model.entity.Pasajero;
import com.duoc.ms_pasajeros.service.PasajeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/pasajeros")
@RequiredArgsConstructor
@Tag(name = "Pasajeros", description = "Gestión de pasajeros")
@SecurityRequirement(name = "Bearer Authentication")
public class PasajeroController {

    private static final Logger log = LoggerFactory.getLogger(PasajeroController.class);

    private final PasajeroService service;

    // GET /api/pasajeros → lista todos
    @GetMapping
    @Operation(
        summary = "Listar todos los pasajeros",
        description = "Obtiene la lista completa de pasajeros registrados en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pasajeros obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token inválido o ausente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Pasajero>>> listar() {
        log.info("GET /api/pasajeros - solicitando lista de pasajeros");
        List<Pasajero> pasajeros = service.obtenerTodos();
        
        List<EntityModel<Pasajero>> models = pasajeros.stream()
            .map(p -> EntityModel.of(p,
                linkTo(methodOn(PasajeroController.class).obtenerPorId(p.getId())).withSelfRel(),
                linkTo(methodOn(PasajeroController.class).listar()).withRel("pasajeros-list")
            ))
            .toList();
        
        CollectionModel<EntityModel<Pasajero>> collection = CollectionModel.of(models,
            linkTo(methodOn(PasajeroController.class).listar()).withSelfRel()
        );
        
        return ResponseEntity.ok(collection);
    }

    // GET /api/pasajeros/{id} → busca uno por ID
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener pasajero por ID",
        description = "Busca y retorna un pasajero específico por su identificador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    public ResponseEntity<EntityModel<Pasajero>> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pasajeros/{} - buscando pasajero", id);
        Pasajero pasajero = service.obtenerPorId(id);
        
        EntityModel<Pasajero> model = EntityModel.of(pasajero,
            linkTo(methodOn(PasajeroController.class).obtenerPorId(id)).withSelfRel(),
            linkTo(methodOn(PasajeroController.class).listar()).withRel("pasajeros-list"),
            linkTo(methodOn(PasajeroController.class).actualizar(id, null)).withRel("update"),
            linkTo(methodOn(PasajeroController.class).eliminar(id)).withRel("delete")
        );
        
        return ResponseEntity.ok(model);
    }

    // POST /api/pasajeros → crea uno nuevo
    @PostMapping
    @Operation(
        summary = "Crear nuevo pasajero",
        description = "Registra un nuevo pasajero en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pasajero creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<EntityModel<Pasajero>> crear(@Valid @RequestBody PasajeroDTO dto) {
        log.info("POST /api/pasajeros - creando nuevo pasajero");
        Pasajero nuevo = service.guardar(dto);
        
        EntityModel<Pasajero> model = EntityModel.of(nuevo,
            linkTo(methodOn(PasajeroController.class).obtenerPorId(nuevo.getId())).withSelfRel(),
            linkTo(methodOn(PasajeroController.class).listar()).withRel("pasajeros-list")
        );
        
        return new ResponseEntity<>(model, HttpStatus.CREATED);
    }

    // PUT /api/pasajeros/{id} → actualiza uno existente
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar pasajero",
        description = "Actualiza los datos de un pasajero existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pasajero actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    public ResponseEntity<EntityModel<Pasajero>> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody PasajeroDTO dto) {
        log.info("PUT /api/pasajeros/{} - actualizando pasajero", id);
        Pasajero actualizado = service.actualizar(id, dto);
        
        EntityModel<Pasajero> model = EntityModel.of(actualizado,
            linkTo(methodOn(PasajeroController.class).obtenerPorId(id)).withSelfRel(),
            linkTo(methodOn(PasajeroController.class).listar()).withRel("pasajeros-list")
        );
        
        return ResponseEntity.ok(model);
    }

    // DELETE /api/pasajeros/{id} → elimina uno
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar pasajero",
        description = "Elimina un pasajero del sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pasajero eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Pasajero no encontrado")
    })
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pasajeros/{} - eliminando pasajero", id);
        service.eliminar(id);
        return ResponseEntity.ok("Pasajero con id " + id + " eliminado correctamente");
    }

}

