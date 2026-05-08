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

@RestController
@RequestMapping("/api/conductores")
@RequiredArgsConstructor
public class ConductorController {

    private static final Logger log = LoggerFactory.getLogger(ConductorController.class);

    private final ConductorService service;

    // GET /api/conductores → lista todos
    @GetMapping
    public ResponseEntity<List<Conductor>> listar() {
        log.info("GET /api/conductores - solicitando lista de conductores");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/conductores/{id} → busca uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Conductor> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/conductores/{} - buscando conductor", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/conductores → crea uno nuevo
    @PostMapping
    public ResponseEntity<Conductor> crear(@Valid @RequestBody ConductorDTO dto) {
        log.info("POST /api/conductores - creando nuevo conductor");
        Conductor nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/conductores/{id} → actualiza uno existente
    @PutMapping("/{id}")
    public ResponseEntity<Conductor> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody ConductorDTO dto) {
        log.info("PUT /api/conductores/{} - actualizando conductor", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/conductores/{id} → elimina uno
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/conductores/{} - eliminando conductor", id);
        service.eliminar(id);
        return ResponseEntity.ok("Conductor con id " + id + " eliminado correctamente");
    }

}
