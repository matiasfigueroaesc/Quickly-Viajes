package com.duoc.ms_incidencias.controller;

import com.duoc.ms_incidencias.model.dto.EstadoIncidenciaDTO;
import com.duoc.ms_incidencias.model.dto.IncidenciaDTO;
import com.duoc.ms_incidencias.model.entity.Incidencia;
import com.duoc.ms_incidencias.service.IncidenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
@RequiredArgsConstructor
public class IncidenciaController {

    private static final Logger log = LoggerFactory.getLogger(IncidenciaController.class);

    private final IncidenciaService service;

    // GET /api/incidencias → lista todas las incidencias
    @GetMapping
    public ResponseEntity<List<Incidencia>> listar() {
        log.info("GET /api/incidencias - solicitando lista de incidencias");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/incidencias/{id} → busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Incidencia> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/incidencias/{} - buscando incidencia", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/incidencias/viaje/{viajeId} → incidencias de un viaje
    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Incidencia>> obtenerPorViaje(@PathVariable Long viajeId) {
        log.info("GET /api/incidencias/viaje/{} - listando incidencias del viaje", viajeId);
        return ResponseEntity.ok(service.obtenerPorViaje(viajeId));
    }

    // GET /api/incidencias/estado/{estado} → filtra por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Incidencia>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/incidencias/estado/{} - listando incidencias por estado", estado);
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // GET /api/incidencias/reportante/{reportadoPor} → incidencias de un usuario
    @GetMapping("/reportante/{reportadoPor}")
    public ResponseEntity<List<Incidencia>> obtenerPorReportante(@PathVariable Long reportadoPor) {
        log.info("GET /api/incidencias/reportante/{} - listando incidencias", reportadoPor);
        return ResponseEntity.ok(service.obtenerPorReportante(reportadoPor));
    }

    // POST /api/incidencias → registra una nueva incidencia
    @PostMapping
    public ResponseEntity<Incidencia> crear(@Valid @RequestBody IncidenciaDTO dto) {
        log.info("POST /api/incidencias - registrando nueva incidencia");
        Incidencia nueva = service.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/incidencias/{id} → edita tipo y descripción (solo si no está cerrada)
    @PutMapping("/{id}")
    public ResponseEntity<Incidencia> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody IncidenciaDTO dto) {
        log.info("PUT /api/incidencias/{} - actualizando incidencia", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // PATCH /api/incidencias/{id}/estado → cambia el estado de la incidencia
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Incidencia> actualizarEstado(@PathVariable Long id,
                                                        @Valid @RequestBody EstadoIncidenciaDTO dto) {
        log.info("PATCH /api/incidencias/{}/estado - actualizando estado a {}", id, dto.getEstado());
        return ResponseEntity.ok(service.actualizarEstado(id, dto));
    }

    // DELETE /api/incidencias/{id} → elimina una incidencia
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/incidencias/{} - eliminando incidencia", id);
        service.eliminar(id);
        return ResponseEntity.ok("Incidencia con id " + id + " eliminada correctamente");
    }
}
