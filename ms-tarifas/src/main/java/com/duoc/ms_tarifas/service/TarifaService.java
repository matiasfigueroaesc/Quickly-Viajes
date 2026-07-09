package com.duoc.ms_tarifas.service;

import com.duoc.ms_tarifas.exception.TarifaNotFoundException;
import com.duoc.ms_tarifas.model.dto.TarifaDTO;
import com.duoc.ms_tarifas.model.entity.Tarifa;
import com.duoc.ms_tarifas.repository.TarifaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TarifaService {

    private static final Logger log = LoggerFactory.getLogger(TarifaService.class);

    private final TarifaRepository repository;

    public List<Tarifa> obtenerTodas() {
        log.info("Listando todas las tarifas");
        List<Tarifa> lista = repository.findAll();
        log.info("Se encontraron {} tarifas", lista.size());
        return lista;
    }

    public Tarifa obtenerPorId(Long id) {
        log.info("Buscando tarifa con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró tarifa con id: {}", id);
            return new TarifaNotFoundException(id);
        });
    }

    public Tarifa guardar(TarifaDTO dto) {
        log.info("Intentando registrar tarifa con nombre: {}", dto.getNombre());

        // Regla de negocio: no permitir nombres duplicados
        if (repository.existsByNombre(dto.getNombre())) {
            log.warn("Ya existe una tarifa con el nombre: {}", dto.getNombre());
            throw new IllegalArgumentException("Ya existe una tarifa registrada con el nombre: " + dto.getNombre());
        }

        Tarifa tarifa = new Tarifa();
        tarifa.setNombre(dto.getNombre());
        tarifa.setPrecioBase(dto.getPrecioBase());
        tarifa.setDescripcion(dto.getDescripcion());

        Tarifa guardada = repository.save(tarifa);
        log.info("Tarifa guardada correctamente con id: {}", guardada.getId());
        return guardada;
    }

    public Tarifa actualizar(Long id, TarifaDTO dto) {
        log.info("Actualizando tarifa con id: {}", id);

        Tarifa tarifa = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: tarifa con id {} no existe", id);
            return new TarifaNotFoundException(id);
        });

        tarifa.setNombre(dto.getNombre());
        tarifa.setPrecioBase(dto.getPrecioBase());
        tarifa.setDescripcion(dto.getDescripcion());

        Tarifa actualizada = repository.save(tarifa);
        log.info("Tarifa con id {} actualizada correctamente", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar tarifa con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: tarifa con id {} no existe", id);
            throw new TarifaNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Tarifa con id {} eliminada correctamente", id);
    }

}
