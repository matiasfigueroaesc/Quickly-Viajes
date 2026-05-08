package com.duoc.ms_notificaciones.controller;

import com.duoc.ms_notificaciones.model.dto.NotificacionDTO;
import com.duoc.ms_notificaciones.model.entity.Notificacion;
import com.duoc.ms_notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);

    private final NotificacionService service;

    // GET /api/notificaciones → lista todas
    @GetMapping
    public ResponseEntity<List<Notificacion>> listar() {
        log.info("GET /api/notificaciones - solicitando lista de notificaciones");
        return ResponseEntity.ok(service.obtenerTodas());
    }

    // GET /api/notificaciones/{id} → busca una por ID
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/notificaciones/{} - buscando notificación", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // GET /api/notificaciones/viaje/{viajeId} → busca por viaje
    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<List<Notificacion>> obtenerPorViaje(@PathVariable Long viajeId) {
        log.info("GET /api/notificaciones/viaje/{} - buscando notificaciones por viaje", viajeId);
        return ResponseEntity.ok(service.obtenerPorViajeId(viajeId));
    }

    // GET /api/notificaciones/estado/{estado} → filtra por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> obtenerPorEstado(@PathVariable String estado) {
        log.info("GET /api/notificaciones/estado/{} - buscando por estado", estado);
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    // POST /api/notificaciones → envía una nueva notificación
    @PostMapping
    public ResponseEntity<Notificacion> enviar(@Valid @RequestBody NotificacionDTO dto) {
        log.info("POST /api/notificaciones - enviando nueva notificación");
        Notificacion nueva = service.enviar(dto);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // PUT /api/notificaciones/{id} → actualiza una existente
    @PutMapping("/{id}")
    public ResponseEntity<Notificacion> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody NotificacionDTO dto) {
        log.info("PUT /api/notificaciones/{} - actualizando notificación", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    // DELETE /api/notificaciones/{id} → elimina una
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{} - eliminando notificación", id);
        service.eliminar(id);
        return ResponseEntity.ok("Notificación con id " + id + " eliminada correctamente");
    }

}
