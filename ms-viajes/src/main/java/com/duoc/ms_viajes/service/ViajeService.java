package com.duoc.ms_viajes.service;

import com.duoc.ms_viajes.client.MicroservicioClient;
import com.duoc.ms_viajes.exception.ViajeNotFoundException;
import com.duoc.ms_viajes.model.dto.ViajeDTO;
import com.duoc.ms_viajes.model.dto.ViajeDetalleDTO;
import com.duoc.ms_viajes.model.entity.Viaje;
import com.duoc.ms_viajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ViajeService {

    private static final Logger log = LoggerFactory.getLogger(ViajeService.class);

    // Estados válidos del ciclo de vida de un viaje
    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "EN_CURSO", "COMPLETADO", "CANCELADO");

    private final ViajeRepository repository;
    private final MicroservicioClient microservicioClient;

    public List<Viaje> obtenerTodos() {
        log.info("Listando todos los viajes");
        List<Viaje> lista = repository.findAll();
        log.info("Se encontraron {} viajes", lista.size());
        return lista;
    }

    public Viaje obtenerPorId(Long id) {
        log.info("Buscando viaje con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró viaje con id: {}", id);
            return new ViajeNotFoundException(id);
        });
    }

    /**
     * Retorna el viaje enriquecido con información de pasajero, conductor y tarifa
     * consultando los microservicios correspondientes vía WebClient.
     */
    public ViajeDetalleDTO obtenerDetallePorId(Long id) {
        log.info("Obteniendo detalle completo del viaje id: {}", id);

        Viaje viaje = obtenerPorId(id);

        // Consultar datos remotos en paralelo lógico (llamadas sincrónicas)
        log.info("Consultando datos remotos para viaje id: {}", id);
        Map<String, Object> pasajero   = microservicioClient.obtenerPasajero(viaje.getPasajeroId());
        Map<String, Object> conductor  = microservicioClient.obtenerConductor(viaje.getConductorId());
        Map<String, Object> tarifa     = microservicioClient.obtenerTarifa(viaje.getTarifaId());

        // Construir DTO de respuesta enriquecido
        ViajeDetalleDTO detalle = new ViajeDetalleDTO();
        detalle.setId(viaje.getId());
        detalle.setOrigen(viaje.getOrigen());
        detalle.setDestino(viaje.getDestino());
        detalle.setEstado(viaje.getEstado());
        detalle.setFechaInicio(viaje.getFechaInicio());
        detalle.setFechaFin(viaje.getFechaFin());
        detalle.setMontoTotal(viaje.getMontoTotal());

        // Datos del pasajero
        detalle.setPasajeroId(viaje.getPasajeroId());
        detalle.setPasajeroNombre((String) pasajero.get("nombre"));
        detalle.setPasajeroEmail((String) pasajero.get("email"));

        // Datos del conductor
        detalle.setConductorId(viaje.getConductorId());
        detalle.setConductorNombre((String) conductor.get("nombre"));
        detalle.setConductorLicencia((String) conductor.get("licencia"));

        // Datos de la tarifa
        detalle.setTarifaId(viaje.getTarifaId());
        detalle.setTarifaNombre((String) tarifa.get("nombre"));
        Object precioBase = tarifa.get("precioBase");
        if (precioBase != null) {
            detalle.setTarifaPrecioBase(new BigDecimal(precioBase.toString()));
        }

        log.info("Detalle del viaje id {} construido correctamente", id);
        return detalle;
    }

    public List<Viaje> obtenerPorPasajero(Long pasajeroId) {
        log.info("Buscando viajes del pasajero id: {}", pasajeroId);
        List<Viaje> lista = repository.findByPasajeroId(pasajeroId);
        log.info("Se encontraron {} viajes para el pasajero {}", lista.size(), pasajeroId);
        return lista;
    }

    public List<Viaje> obtenerPorConductor(Long conductorId) {
        log.info("Buscando viajes del conductor id: {}", conductorId);
        List<Viaje> lista = repository.findByConductorId(conductorId);
        log.info("Se encontraron {} viajes para el conductor {}", lista.size(), conductorId);
        return lista;
    }

    public Viaje guardar(ViajeDTO dto) {
        log.info("Intentando registrar nuevo viaje de '{}' hacia '{}'", dto.getOrigen(), dto.getDestino());

        // Regla de negocio: validar que el estado sea un valor permitido
        validarEstado(dto.getEstado());

        // Regla de negocio: un conductor no puede tener dos viajes EN_CURSO simultáneamente
        if ("EN_CURSO".equals(dto.getEstado()) &&
                repository.existsByConductorIdAndEstado(dto.getConductorId(), "EN_CURSO")) {
            log.warn("El conductor id {} ya tiene un viaje EN_CURSO", dto.getConductorId());
            throw new IllegalArgumentException(
                    "El conductor con id " + dto.getConductorId() + " ya tiene un viaje en curso activo");
        }

        // Regla de negocio: verificar que el pasajero, conductor y tarifa existen en sus respectivos microservicios
        log.info("Verificando existencia de pasajero, conductor y tarifa en microservicios...");
        microservicioClient.obtenerPasajero(dto.getPasajeroId());
        microservicioClient.obtenerConductor(dto.getConductorId());
        microservicioClient.obtenerTarifa(dto.getTarifaId());

        Viaje viaje = new Viaje();
        viaje.setPasajeroId(dto.getPasajeroId());
        viaje.setConductorId(dto.getConductorId());
        viaje.setTarifaId(dto.getTarifaId());
        viaje.setOrigen(dto.getOrigen());
        viaje.setDestino(dto.getDestino());
        viaje.setEstado(dto.getEstado());
        viaje.setFechaInicio(dto.getFechaInicio());
        viaje.setFechaFin(dto.getFechaFin());
        viaje.setMontoTotal(dto.getMontoTotal());

        Viaje guardado = repository.save(viaje);
        log.info("Viaje guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    public Viaje actualizar(Long id, ViajeDTO dto) {
        log.info("Actualizando viaje con id: {}", id);

        // Regla de negocio: validar que el estado sea un valor permitido
        validarEstado(dto.getEstado());

        Viaje viaje = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: viaje con id {} no existe", id);
            return new ViajeNotFoundException(id);
        });

        // Regla de negocio: un viaje COMPLETADO o CANCELADO no puede ser modificado
        if ("COMPLETADO".equals(viaje.getEstado()) || "CANCELADO".equals(viaje.getEstado())) {
            log.warn("Intento de modificar viaje con estado final: {}", viaje.getEstado());
            throw new IllegalArgumentException(
                    "No se puede modificar un viaje con estado: " + viaje.getEstado());
        }

        viaje.setPasajeroId(dto.getPasajeroId());
        viaje.setConductorId(dto.getConductorId());
        viaje.setTarifaId(dto.getTarifaId());
        viaje.setOrigen(dto.getOrigen());
        viaje.setDestino(dto.getDestino());
        viaje.setEstado(dto.getEstado());
        viaje.setFechaInicio(dto.getFechaInicio());
        viaje.setFechaFin(dto.getFechaFin());
        viaje.setMontoTotal(dto.getMontoTotal());

        Viaje actualizado = repository.save(viaje);
        log.info("Viaje con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar viaje con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: viaje con id {} no existe", id);
            throw new ViajeNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Viaje con id {} eliminado correctamente", id);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            log.warn("Estado inválido recibido: '{}'", estado);
            throw new IllegalArgumentException(
                    "Estado inválido: '" + estado + "'. Los valores permitidos son: " + ESTADOS_VALIDOS);
        }
    }
}
