package com.duoc.ms_pasajeros.controller;

import com.duoc.ms_pasajeros.model.dto.PasajeroDTO;
import com.duoc.ms_pasajeros.model.entity.Pasajero;
import com.duoc.ms_pasajeros.service.PasajeroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
@RequiredArgsConstructor
public class PasajeroController {

    private static final Logger log = LoggerFactory.getLogger(PasajeroController.class);

    private final PasajeroService service;

    // GET /api/pasajeros → lista todos
    @GetMapping
    public ResponseEntity<List<Pasajero>> listar() {
        log.info("GET /api/pasajeros - solicitando lista de pasajeros");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/pasajeros/{id} → busca uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pasajero> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pasajeros/{} - buscando pasajero", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/pasajeros → crea uno nuevo
    @PostMapping
    public ResponseEntity<Pasajero> crear(@Valid @RequestBody PasajeroDTO dto) {
        log.info("POST /api/pasajeros - creando nuevo pasajero");
        Pasajero nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/pasajeros/{id} → actualiza uno existente
    @PutMapping("/{id}")
    public ResponseEntity<Pasajero> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody PasajeroDTO dto) {
        log.info("PUT /api/pasajeros/{} - actualizando pasajero", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/pasajeros/{id} → elimina uno
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pasajeros/{} - eliminando pasajero", id);
        service.eliminar(id);
        return ResponseEntity.ok("Pasajero con id " + id + " eliminado correctamente");
    }

}
