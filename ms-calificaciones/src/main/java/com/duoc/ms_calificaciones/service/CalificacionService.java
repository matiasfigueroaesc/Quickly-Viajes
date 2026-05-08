package com.duoc.ms_calificaciones.service;

import com.duoc.ms_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.ms_calificaciones.model.dto.CalificacionDTO;
import com.duoc.ms_calificaciones.model.entity.Calificacion;
import com.duoc.ms_calificaciones.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private static final Logger log = LoggerFactory.getLogger(CalificacionService.class);

    private final CalificacionRepository repository;

    public List<Calificacion> obtenerTodas() {
        log.info("Listando todas las calificaciones");
        List<Calificacion> lista = repository.findAll();
        log.info("Se encontraron {} calificaciones", lista.size());
        return lista;
    }

    public Calificacion obtenerPorId(Long id) {
        log.info("Buscando calificación con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró calificación con id: {}", id);
            return new CalificacionNotFoundException(id);
        });
    }

    public List<Calificacion> obtenerPorConductor(Long conductorId) {
        log.info("Listando calificaciones del conductor con id: {}", conductorId);
        List<Calificacion> lista = repository.findByConductorId(conductorId);
        log.info("Se encontraron {} calificaciones para el conductor {}", lista.size(), conductorId);
        return lista;
    }

    public List<Calificacion> obtenerPorPasajero(Long pasajeroId) {
        log.info("Listando calificaciones del pasajero con id: {}", pasajeroId);
        return repository.findByPasajeroId(pasajeroId);
    }

    public Double obtenerPromedioConductor(Long conductorId) {
        log.info("Calculando promedio de calificaciones del conductor: {}", conductorId);
        Double promedio = repository.promedioCalificacionConductor(conductorId);
        log.info("Promedio del conductor {}: {}", conductorId, promedio);
        return promedio != null ? promedio : 0.0;
    }

    public Calificacion guardar(CalificacionDTO dto) {
        log.info("Intentando registrar calificación para viaje {} por pasajero {}", dto.getViajeId(), dto.getPasajeroId());

        // Regla de negocio: un pasajero no puede calificar el mismo viaje dos veces
        if (repository.existsByViajeIdAndPasajeroId(dto.getViajeId(), dto.getPasajeroId())) {
            log.warn("El pasajero {} ya calificó el viaje {}", dto.getPasajeroId(), dto.getViajeId());
            throw new IllegalArgumentException(
                    "El pasajero con id " + dto.getPasajeroId() + " ya calificó el viaje con id " + dto.getViajeId());
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setViajeId(dto.getViajeId());
        calificacion.setPasajeroId(dto.getPasajeroId());
        calificacion.setConductorId(dto.getConductorId());
        calificacion.setPuntaje(dto.getPuntaje());
        calificacion.setComentario(dto.getComentario());

        Calificacion guardada = repository.save(calificacion);
        log.info("Calificación registrada correctamente con id: {}", guardada.getId());
        return guardada;
    }

    public Calificacion actualizar(Long id, CalificacionDTO dto) {
        log.info("Actualizando calificación con id: {}", id);

        Calificacion calificacion = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: calificación con id {} no existe", id);
            return new CalificacionNotFoundException(id);
        });

        calificacion.setPuntaje(dto.getPuntaje());
        calificacion.setComentario(dto.getComentario());

        Calificacion actualizada = repository.save(calificacion);
        log.info("Calificación con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar calificación con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: calificación con id {} no existe", id);
            throw new CalificacionNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Calificación con id {} eliminada correctamente", id);
    }
}
