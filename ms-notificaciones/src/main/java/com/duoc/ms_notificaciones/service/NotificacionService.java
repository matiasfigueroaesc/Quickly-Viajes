package com.duoc.ms_notificaciones.service;

import com.duoc.ms_notificaciones.exception.NotificacionNotFoundException;
import com.duoc.ms_notificaciones.model.dto.NotificacionDTO;
import com.duoc.ms_notificaciones.model.entity.Notificacion;
import com.duoc.ms_notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository repository;

    public List<Notificacion> obtenerTodas() {
        log.info("Listando todas las notificaciones");
        List<Notificacion> lista = repository.findAll();
        log.info("Se encontraron {} notificaciones", lista.size());
        return lista;
    }

    public Notificacion obtenerPorId(Long id) {
        log.info("Buscando notificación con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró notificación con id: {}", id);
            return new NotificacionNotFoundException(id);
        });
    }

    public List<Notificacion> obtenerPorViajeId(Long viajeId) {
        log.info("Buscando notificaciones para viaje id: {}", viajeId);
        List<Notificacion> lista = repository.findByViajeId(viajeId);
        log.info("Se encontraron {} notificaciones para viaje id: {}", lista.size(), viajeId);
        return lista;
    }

    public List<Notificacion> obtenerPorEstado(String estado) {
        log.info("Buscando notificaciones con estado: {}", estado);
        List<Notificacion> lista = repository.findByEstado(estado.toUpperCase());
        log.info("Se encontraron {} notificaciones con estado: {}", lista.size(), estado);
        return lista;
    }

    public Notificacion enviar(NotificacionDTO dto) {
        log.info("Registrando notificación tipo '{}' para viaje id: {}", dto.getTipo(), dto.getViajeId());

        Notificacion notificacion = new Notificacion();
        notificacion.setViajeId(dto.getViajeId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setCanal(dto.getCanal());
        notificacion.setEstado("ENVIADA");
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion guardada = repository.save(notificacion);
        log.info("Notificación enviada correctamente con id: {}", guardada.getId());
        return guardada;
    }

    public Notificacion actualizar(Long id, NotificacionDTO dto) {
        log.info("Actualizando notificación con id: {}", id);

        Notificacion notificacion = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: notificación con id {} no existe", id);
            return new NotificacionNotFoundException(id);
        });

        notificacion.setViajeId(dto.getViajeId());
        notificacion.setTipo(dto.getTipo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setCanal(dto.getCanal());

        Notificacion actualizada = repository.save(notificacion);
        log.info("Notificación con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar notificación con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: notificación con id {} no existe", id);
            throw new NotificacionNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Notificación con id {} eliminada correctamente", id);
    }

}
