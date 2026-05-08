package com.duoc.ms_incidencias.service;

import com.duoc.ms_incidencias.exception.IncidenciaNotFoundException;
import com.duoc.ms_incidencias.model.dto.EstadoIncidenciaDTO;
import com.duoc.ms_incidencias.model.dto.IncidenciaDTO;
import com.duoc.ms_incidencias.model.entity.Incidencia;
import com.duoc.ms_incidencias.repository.IncidenciaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidenciaService {

    private static final Logger log = LoggerFactory.getLogger(IncidenciaService.class);

    private final IncidenciaRepository repository;

    public List<Incidencia> obtenerTodas() {
        log.info("Listando todas las incidencias");
        List<Incidencia> lista = repository.findAll();
        log.info("Se encontraron {} incidencias", lista.size());
        return lista;
    }

    public Incidencia obtenerPorId(Long id) {
        log.info("Buscando incidencia con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró incidencia con id: {}", id);
            return new IncidenciaNotFoundException(id);
        });
    }

    public List<Incidencia> obtenerPorViaje(Long viajeId) {
        log.info("Listando incidencias del viaje con id: {}", viajeId);
        return repository.findByViajeId(viajeId);
    }

    public List<Incidencia> obtenerPorEstado(String estado) {
        log.info("Listando incidencias con estado: {}", estado);
        return repository.findByEstado(estado);
    }

    public List<Incidencia> obtenerPorReportante(Long reportadoPor) {
        log.info("Listando incidencias reportadas por el usuario: {}", reportadoPor);
        return repository.findByReportadoPor(reportadoPor);
    }

    public Incidencia guardar(IncidenciaDTO dto) {
        log.info("Registrando nueva incidencia de tipo {} para el viaje {}", dto.getTipo(), dto.getViajeId());

        Incidencia incidencia = new Incidencia();
        incidencia.setViajeId(dto.getViajeId());
        incidencia.setReportadoPor(dto.getReportadoPor());
        incidencia.setTipo(dto.getTipo());
        incidencia.setDescripcion(dto.getDescripcion());
        incidencia.setEstado("ABIERTA");

        Incidencia guardada = repository.save(incidencia);
        log.info("Incidencia registrada correctamente con id: {}", guardada.getId());
        return guardada;
    }

    public Incidencia actualizarEstado(Long id, EstadoIncidenciaDTO dto) {
        log.info("Actualizando estado de incidencia con id: {} a {}", id, dto.getEstado());

        Incidencia incidencia = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: incidencia con id {} no existe", id);
            return new IncidenciaNotFoundException(id);
        });

        // Regla de negocio: no se puede reabrir una incidencia cerrada
        if ("CERRADA".equals(incidencia.getEstado())) {
            log.warn("Intento de modificar incidencia CERRADA con id: {}", id);
            throw new IllegalArgumentException("No se puede modificar el estado de una incidencia CERRADA");
        }

        // Si pasa a RESUELTA, registrar la fecha de resolución
        if ("RESUELTA".equals(dto.getEstado()) || "CERRADA".equals(dto.getEstado())) {
            incidencia.setFechaResolucion(LocalDateTime.now());
            log.info("Incidencia con id {} marcada como {} con fecha de resolución", id, dto.getEstado());
        }

        incidencia.setEstado(dto.getEstado());
        Incidencia actualizada = repository.save(incidencia);
        log.info("Estado de incidencia con id {} actualizado correctamente a {}", id, dto.getEstado());
        return actualizada;
    }

    public Incidencia actualizar(Long id, IncidenciaDTO dto) {
        log.info("Actualizando incidencia con id: {}", id);

        Incidencia incidencia = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: incidencia con id {} no existe", id);
            return new IncidenciaNotFoundException(id);
        });

        // Regla de negocio: no se puede editar una incidencia cerrada o resuelta
        if ("CERRADA".equals(incidencia.getEstado()) || "RESUELTA".equals(incidencia.getEstado())) {
            log.warn("Intento de editar incidencia en estado {} con id: {}", incidencia.getEstado(), id);
            throw new IllegalArgumentException("No se puede editar una incidencia en estado " + incidencia.getEstado());
        }

        incidencia.setTipo(dto.getTipo());
        incidencia.setDescripcion(dto.getDescripcion());

        Incidencia actualizada = repository.save(incidencia);
        log.info("Incidencia con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar incidencia con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: incidencia con id {} no existe", id);
            throw new IncidenciaNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Incidencia con id {} eliminada correctamente", id);
    }
}
