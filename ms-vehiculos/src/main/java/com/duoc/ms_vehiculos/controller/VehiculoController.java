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

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private static final Logger log = LoggerFactory.getLogger(VehiculoController.class);

    private final VehiculoService service;

    // GET /api/vehiculos → lista todos
    @GetMapping
    public ResponseEntity<List<Vehiculo>> listar() {
        log.info("GET /api/vehiculos - solicitando lista de vehículos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    // GET /api/vehiculos/{id} → busca uno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/vehiculos/{} - buscando vehículo", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/vehiculos → crea uno nuevo
    @PostMapping
    public ResponseEntity<Vehiculo> crear(@Valid @RequestBody VehiculoDTO dto) {
        log.info("POST /api/vehiculos - creando nuevo vehículo");
        Vehiculo nuevo = service.guardar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // PUT /api/vehiculos/{id} → actualiza uno existente
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable Long id,
                                               @Valid @RequestBody VehiculoDTO dto) {
        log.info("PUT /api/vehiculos/{} - actualizando vehículo", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/vehiculos/{id} → elimina uno
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/vehiculos/{} - eliminando vehículo", id);
        service.eliminar(id);
        return ResponseEntity.ok("Vehículo con id " + id + " eliminado correctamente");
    }

}
