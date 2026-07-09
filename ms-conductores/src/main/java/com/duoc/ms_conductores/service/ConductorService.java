package com.duoc.ms_conductores.service;

import com.duoc.ms_conductores.exception.ConductorNotFoundException;
import com.duoc.ms_conductores.model.dto.ConductorDTO;
import com.duoc.ms_conductores.model.entity.Conductor;
import com.duoc.ms_conductores.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConductorService {

    private static final Logger log = LoggerFactory.getLogger(ConductorService.class);

    private final ConductorRepository repository;

    public List<Conductor> obtenerTodos() {
        log.info("Listando todos los conductores");
        List<Conductor> lista = repository.findAll();
        log.info("Se encontraron {} conductores", lista.size());
        return lista;
    }

    public Conductor obtenerPorId(Long id) {
        log.info("Buscando conductor con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró conductor con id: {}", id);
            return new ConductorNotFoundException(id);
        });
    }

    public Conductor guardar(ConductorDTO dto) {
        log.info("Intentando registrar conductor con email: {}", dto.getEmail());

        // Regla de negocio: no permitir emails duplicados
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Ya existe un conductor con el email: {}", dto.getEmail());
            throw new IllegalArgumentException("Ya existe un conductor registrado con el email: " + dto.getEmail());
        }

        // Regla de negocio: no permitir licencias duplicadas
        if (repository.existsByLicencia(dto.getLicencia())) {
            log.warn("Ya existe un conductor con la licencia: {}", dto.getLicencia());
            throw new IllegalArgumentException("Ya existe un conductor registrado con la licencia: " + dto.getLicencia());
        }

        Conductor conductor = new Conductor();
        conductor.setNombre(dto.getNombre());
        conductor.setEmail(dto.getEmail());
        conductor.setTelefono(dto.getTelefono());
        conductor.setLicencia(dto.getLicencia());
        conductor.setActivo(dto.getActivo());

        Conductor guardado = repository.save(conductor);
        log.info("Conductor guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    public Conductor actualizar(Long id, ConductorDTO dto) {
        log.info("Actualizando conductor con id: {}", id);

        Conductor conductor = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: conductor con id {} no existe", id);
            return new ConductorNotFoundException(id);
        });

        conductor.setNombre(dto.getNombre());
        conductor.setEmail(dto.getEmail());
        conductor.setTelefono(dto.getTelefono());
        conductor.setLicencia(dto.getLicencia());
        conductor.setActivo(dto.getActivo());

        Conductor actualizado = repository.save(conductor);
        log.info("Conductor con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar conductor con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: conductor con id {} no existe", id);
            throw new ConductorNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Conductor con id {} eliminado correctamente", id);
    }

}
