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

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    private final PagoService service;

    // GET /api/pagos → lista todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        log.info("GET /api/pagos - solicitando lista de pagos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/pagos/{id} → busca pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pagos/{} - buscando pago", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/pagos/viaje/{viajeId} → todos los pagos de un viaje específico
    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Pago>> obtenerPorViaje(@PathVariable Long viajeId) {
        log.info("GET /api/pagos/viaje/{} - buscando pagos del viaje", viajeId);
        return ResponseEntity.ok(service.obtenerPorViaje(viajeId));
    }

    // GET /api/pagos/estado/{estado} → filtra pagos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/pagos/estado/{} - filtrando pagos por estado", estado);
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // POST /api/pagos → registra un nuevo pago
    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO dto) {
        log.info("POST /api/pagos - registrando nuevo pago para viaje id: {}", dto.getViajeId());
        Pago nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/pagos/{id} → actualiza un pago existente
    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id,
                                            @Valid @RequestBody PagoDTO dto) {
        log.info("PUT /api/pagos/{} - actualizando pago", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/pagos/{id} → elimina un pago
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{} - eliminando pago", id);
        service.eliminar(id);
        return ResponseEntity.ok("Pago con id " + id + " eliminado correctamente");
    }
}
