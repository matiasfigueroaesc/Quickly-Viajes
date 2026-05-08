package com.duoc.ms_calificaciones.controller;

import com.duoc.ms_calificaciones.model.dto.CalificacionDTO;
import com.duoc.ms_calificaciones.model.entity.Calificacion;
import com.duoc.ms_calificaciones.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private static final Logger log = LoggerFactory.getLogger(CalificacionController.class);

    private final CalificacionService service;

    // GET /api/calificaciones → lista todas
    @GetMapping
    public ResponseEntity<List<Calificacion>> listar() {
        log.info("GET /api/calificaciones - solicitando lista de calificaciones");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/calificaciones/{id} → busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/calificaciones/{} - buscando calificación", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/calificaciones/conductor/{conductorId} → calificaciones de un conductor
    @GetMapping("/conductor/{conductorId}")
    public ResponseEntity<List<Calificacion>> obtenerPorConductor(@PathVariable Long conductorId) {
        log.info("GET /api/calificaciones/conductor/{} - listando calificaciones", conductorId);
        return ResponseEntity.ok(service.obtenerPorConductor(conductorId));
    }

    // GET /api/calificaciones/pasajero/{pasajeroId} → calificaciones de un pasajero
    @GetMapping("/pasajero/{pasajeroId}")
    public ResponseEntity<List<Calificacion>> obtenerPorPasajero(@PathVariable Long pasajeroId) {
        log.info("GET /api/calificaciones/pasajero/{} - listando calificaciones", pasajeroId);
        return ResponseEntity.ok(service.obtenerPorPasajero(pasajeroId));
    }

    // GET /api/calificaciones/conductor/{conductorId}/promedio → promedio de un conductor
    @GetMapping("/conductor/{conductorId}/promedio")
    public ResponseEntity<Double> obtenerPromedioConductor(@PathVariable Long conductorId) {
        log.info("GET /api/calificaciones/conductor/{}/promedio - calculando promedio", conductorId);
        return ResponseEntity.ok(service.obtenerPromedioConductor(conductorId));
    }

    // POST /api/calificaciones → registra una nueva calificación
    @PostMapping
    public ResponseEntity<Calificacion> crear(@Valid @RequestBody CalificacionDTO dto) {
        log.info("POST /api/calificaciones - registrando nueva calificación");
        Calificacion nueva = service.guardar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/calificaciones/{id} → actualiza puntaje y comentario
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody CalificacionDTO dto) {
        log.info("PUT /api/calificaciones/{} - actualizando calificación", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/calificaciones/{id} → elimina una calificación
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/calificaciones/{} - eliminando calificación", id);
        service.eliminar(id);
        return ResponseEntity.ok("Calificación con id " + id + " eliminada correctamente");
    }
}
