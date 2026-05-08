package com.duoc.ms_vehiculos.service;

import com.duoc.ms_vehiculos.exception.VehiculoNotFoundException;
import com.duoc.ms_vehiculos.model.dto.VehiculoDTO;
import com.duoc.ms_vehiculos.model.entity.Vehiculo;
import com.duoc.ms_vehiculos.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private static final Logger log = LoggerFactory.getLogger(VehiculoService.class);

    private final VehiculoRepository repository;

    public List<Vehiculo> obtenerTodos() {
        log.info("Listando todos los vehículos");
        List<Vehiculo> lista = repository.findAll();
        log.info("Se encontraron {} vehículos", lista.size());
        return lista;
    }

    public Vehiculo obtenerPorId(Long id) {
        log.info("Buscando vehículo con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró vehículo con id: {}", id);
            return new VehiculoNotFoundException(id);
        });
    }

    public Vehiculo guardar(VehiculoDTO dto) {
        log.info("Intentando registrar vehículo con patente: {}", dto.getPatente());

        // Regla de negocio: no permitir patentes duplicadas
        if (repository.existsByPatente(dto.getPatente())) {
            log.warn("Ya existe un vehículo con la patente: {}", dto.getPatente());
            throw new IllegalArgumentException("Ya existe un vehículo registrado con la patente: " + dto.getPatente());
        }

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setTipo(dto.getTipo());

        Vehiculo guardado = repository.save(vehiculo);
        log.info("Vehículo guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    public Vehiculo actualizar(Long id, VehiculoDTO dto) {
        log.info("Actualizando vehículo con id: {}", id);

        Vehiculo vehiculo = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: vehículo con id {} no existe", id);
            return new VehiculoNotFoundException(id);
        });

        vehiculo.setPatente(dto.getPatente());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setTipo(dto.getTipo());

        Vehiculo actualizado = repository.save(vehiculo);
        log.info("Vehículo con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar vehículo con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: vehículo con id {} no existe", id);
            throw new VehiculoNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Vehículo con id {} eliminado correctamente", id);
    }

}
