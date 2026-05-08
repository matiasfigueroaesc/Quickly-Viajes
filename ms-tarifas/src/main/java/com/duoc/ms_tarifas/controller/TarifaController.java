package com.duoc.ms_tarifas.controller;

import com.duoc.ms_tarifas.model.dto.TarifaDTO;
import com.duoc.ms_tarifas.model.entity.Tarifa;
import com.duoc.ms_tarifas.service.TarifaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
@RequiredArgsConstructor
public class TarifaController {

    private static final Logger log = LoggerFactory.getLogger(TarifaController.class);

    private final TarifaService service;

    // GET /api/tarifas → lista todas
    @GetMapping
    public ResponseEntity<List<Tarifa>> listar() {
        log.info("GET /api/tarifas - solicitando lista de tarifas");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/tarifas/{id} → busca una por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarifa> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/tarifas/{} - buscando tarifa", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // POST /api/tarifas → crea una nueva
    @PostMapping
    public ResponseEntity<Tarifa> crear(@Valid @RequestBody TarifaDTO dto) {
        log.info("POST /api/tarifas - creando nueva tarifa");
        Tarifa nueva = service.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/tarifas/{id} → actualiza una existente
    @PutMapping("/{id}")
    public ResponseEntity<Tarifa> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody TarifaDTO dto) {
        log.info("PUT /api/tarifas/{} - actualizando tarifa", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/tarifas/{id} → elimina una
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/tarifas/{} - eliminando tarifa", id);
        service.eliminar(id);
        return ResponseEntity.ok("Tarifa con id " + id + " eliminada correctamente");
    }

}
