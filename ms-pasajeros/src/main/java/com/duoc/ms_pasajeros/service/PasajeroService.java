package com.duoc.ms_pasajeros.service;

import com.duoc.ms_pasajeros.exception.PasajeroNotFoundException;
import com.duoc.ms_pasajeros.model.dto.PasajeroDTO;
import com.duoc.ms_pasajeros.model.entity.Pasajero;
import com.duoc.ms_pasajeros.repository.PasajeroRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PasajeroService {

    private static final Logger log = LoggerFactory.getLogger(PasajeroService.class);

    private final PasajeroRepository repository;

    public List<Pasajero> obtenerTodos() {
        log.info("Listando todos los pasajeros");
        List<Pasajero> lista = repository.findAll();
        log.info("Se encontraron {} pasajeros", lista.size());
        return lista;
    }

    public Pasajero obtenerPorId(Long id) {
        log.info("Buscando pasajero con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró pasajero con id: {}", id);
            return new PasajeroNotFoundException(id);
        });
    }

    public Pasajero guardar(PasajeroDTO dto) {
        log.info("Intentando registrar pasajero con email: {}", dto.getEmail());

        // Regla de negocio: no permitir emails duplicados
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Ya existe un pasajero con el email: {}", dto.getEmail());
            throw new IllegalArgumentException("Ya existe un pasajero registrado con el email: " + dto.getEmail());
        }

        Pasajero pasajero = new Pasajero();
        pasajero.setNombre(dto.getNombre());
        pasajero.setEmail(dto.getEmail());
        pasajero.setTelefono(dto.getTelefono());

        Pasajero guardado = repository.save(pasajero);
        log.info("Pasajero guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    public Pasajero actualizar(Long id, PasajeroDTO dto) {
        log.info("Actualizando pasajero con id: {}", id);

        Pasajero pasajero = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: pasajero con id {} no existe", id);
            return new PasajeroNotFoundException(id);
        });

        pasajero.setNombre(dto.getNombre());
        pasajero.setEmail(dto.getEmail());
        pasajero.setTelefono(dto.getTelefono());

        Pasajero actualizado = repository.save(pasajero);
        log.info("Pasajero con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar pasajero con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: pasajero con id {} no existe", id);
            throw new PasajeroNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Pasajero con id {} eliminado correctamente", id);
    }

}
