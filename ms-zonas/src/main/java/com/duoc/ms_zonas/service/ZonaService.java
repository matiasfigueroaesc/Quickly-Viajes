package com.duoc.ms_zonas.service;

import com.duoc.ms_zonas.exception.ZonaNotFoundException;
import com.duoc.ms_zonas.model.dto.ZonaDTO;
import com.duoc.ms_zonas.model.entity.Zona;
import com.duoc.ms_zonas.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private static final Logger log = LoggerFactory.getLogger(ZonaService.class);

    private final ZonaRepository repository;

    public List<Zona> obtenerTodas() {
        log.info("Listando todas las zonas");
        List<Zona> lista = repository.findAll();
        log.info("Se encontraron {} zonas", lista.size());
        return lista;
    }

    public List<Zona> obtenerActivas() {
        log.info("Listando zonas activas");
        List<Zona> lista = repository.findByActivaTrue();
        log.info("Se encontraron {} zonas activas", lista.size());
        return lista;
    }

    public Zona obtenerPorId(Long id) {
        log.info("Buscando zona con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró zona con id: {}", id);
            return new ZonaNotFoundException(id);
        });
    }

    public Zona guardar(ZonaDTO dto) {
        log.info("Intentando registrar zona con nombre: {}", dto.getNombre());

        // Regla de negocio: no permitir nombres duplicados
        if (repository.existsByNombre(dto.getNombre())) {
            log.warn("Ya existe una zona con el nombre: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe una zona registrada con el nombre: " + dto.getNombre());
        }

        Zona zona = new Zona();
        zona.setNombre(dto.getNombre());
        zona.setDescripcion(dto.getDescripcion());
        zona.setActiva(dto.getActiva());

        Zona guardada = repository.save(zona);
        log.info("Zona guardada correctamente con id: {}", guardada.getId());
        return guardada;
    }

    public Zona actualizar(Long id, ZonaDTO dto) {
        log.info("Actualizando zona con id: {}", id);

        Zona zona = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: zona con id {} no existe", id);
            return new ZonaNotFoundException(id);
        });

        // Regla de negocio: si cambia el nombre, verificar que no esté ya en uso
        if (!zona.getNombre().equals(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
            log.warn("Ya existe otra zona con el nombre: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe una zona registrada con el nombre: " + dto.getNombre());
        }

        zona.setNombre(dto.getNombre());
        zona.setDescripcion(dto.getDescripcion());
        zona.setActiva(dto.getActiva());

        Zona actualizada = repository.save(zona);
        log.info("Zona con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar zona con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: zona con id {} no existe", id);
            throw new ZonaNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Zona con id {} eliminada correctamente", id);
    }
}
