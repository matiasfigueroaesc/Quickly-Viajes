package com.duoc.ms_pagos.service;

import com.duoc.ms_pagos.client.ViajeClient;
import com.duoc.ms_pagos.exception.PagoNotFoundException;
import com.duoc.ms_pagos.model.dto.PagoDTO;
import com.duoc.ms_pagos.model.entity.Pago;
import com.duoc.ms_pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    // Métodos de pago válidos
    private static final Set<String> METODOS_VALIDOS =
            Set.of("TARJETA_CREDITO", "TARJETA_DEBITO", "EFECTIVO", "TRANSFERENCIA");

    // Estados válidos del ciclo de vida de un pago
    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "COMPLETADO", "RECHAZADO", "REEMBOLSADO");

    private final PagoRepository repository;
    private final ViajeClient viajeClient;

    public List<Pago> obtenerTodos() {
        log.info("Listando todos los pagos");
        List<Pago> lista = repository.findAll();
        log.info("Se encontraron {} pagos", lista.size());
        return lista;
    }

    public Pago obtenerPorId(Long id) {
        log.info("Buscando pago con id: {}", id);
        return repository.findById(id).orElseThrow(() -> {
            log.warn("No se encontró pago con id: {}", id);
            return new PagoNotFoundException(id);
        });
    }

    public List<Pago> obtenerPorViaje(Long viajeId) {
        log.info("Buscando pagos del viaje id: {}", viajeId);
        List<Pago> lista = repository.findByViajeId(viajeId);
        log.info("Se encontraron {} pagos para el viaje {}", lista.size(), viajeId);
        return lista;
    }

    public List<Pago> obtenerPorEstado(String estado) {
        log.info("Buscando pagos con estado: {}", estado);
        validarEstado(estado);
        List<Pago> lista = repository.findByEstado(estado);
        log.info("Se encontraron {} pagos con estado '{}'", lista.size(), estado);
        return lista;
    }

    public Pago guardar(PagoDTO dto) {
        log.info("Intentando registrar pago para viaje id: {}", dto.getViajeId());

        // Regla de negocio: validar método de pago y estado
        validarMetodoPago(dto.getMetodoPago());
        validarEstado(dto.getEstado());

        // Regla de negocio: verificar que el viaje existe en ms-viajes
        log.info("Verificando existencia del viaje {} en ms-viajes...", dto.getViajeId());
        Map<String, Object> viaje = viajeClient.obtenerViaje(dto.getViajeId());

        // Regla de negocio: solo se puede pagar un viaje COMPLETADO o EN_CURSO
        String estadoViaje = (String) viaje.get("estado");
        if (!"COMPLETADO".equals(estadoViaje) && !"EN_CURSO".equals(estadoViaje)) {
            log.warn("Intento de pago sobre viaje con estado no pagable: '{}'", estadoViaje);
            throw new IllegalArgumentException(
                    "No se puede registrar un pago para un viaje con estado: " + estadoViaje +
                    ". Solo se permiten viajes EN_CURSO o COMPLETADO");
        }

        // Regla de negocio: no permitir un segundo pago COMPLETADO para el mismo viaje
        if ("COMPLETADO".equals(dto.getEstado()) &&
                repository.existsByViajeIdAndEstado(dto.getViajeId(), "COMPLETADO")) {
            log.warn("El viaje id {} ya tiene un pago COMPLETADO registrado", dto.getViajeId());
            throw new IllegalArgumentException(
                    "El viaje con id " + dto.getViajeId() + " ya cuenta con un pago completado");
        }

        Pago pago = new Pago();
        pago.setViajeId(dto.getViajeId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(dto.getEstado());
        pago.setFechaPago(dto.getFechaPago());

        Pago guardado = repository.save(pago);
        log.info("Pago guardado correctamente con id: {}", guardado.getId());
        return guardado;
    }

    public Pago actualizar(Long id, PagoDTO dto) {
        log.info("Actualizando pago con id: {}", id);

        // Regla de negocio: validar método de pago y estado
        validarMetodoPago(dto.getMetodoPago());
        validarEstado(dto.getEstado());

        Pago pago = repository.findById(id).orElseThrow(() -> {
            log.warn("No se puede actualizar: pago con id {} no existe", id);
            return new PagoNotFoundException(id);
        });

        // Regla de negocio: un pago COMPLETADO o REEMBOLSADO no puede ser modificado
        if ("COMPLETADO".equals(pago.getEstado()) || "REEMBOLSADO".equals(pago.getEstado())) {
            log.warn("Intento de modificar pago con estado final: {}", pago.getEstado());
            throw new IllegalArgumentException(
                    "No se puede modificar un pago con estado: " + pago.getEstado());
        }

        pago.setViajeId(dto.getViajeId());
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(dto.getEstado());
        pago.setFechaPago(dto.getFechaPago());

        Pago actualizado = repository.save(pago);
        log.info("Pago con id {} actualizado correctamente", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        log.info("Intentando eliminar pago con id: {}", id);

        if (!repository.existsById(id)) {
            log.warn("No se puede eliminar: pago con id {} no existe", id);
            throw new PagoNotFoundException(id);
        }

        repository.deleteById(id);
        log.info("Pago con id {} eliminado correctamente", id);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de apoyo
    // -------------------------------------------------------------------------

    private void validarMetodoPago(String metodoPago) {
        if (!METODOS_VALIDOS.contains(metodoPago)) {
            log.warn("Método de pago inválido recibido: '{}'", metodoPago);
            throw new IllegalArgumentException(
                    "Método de pago inválido: '" + metodoPago + "'. Los valores permitidos son: " + METODOS_VALIDOS);
        }
    }

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado)) {
            log.warn("Estado de pago inválido recibido: '{}'", estado);
            throw new IllegalArgumentException(
                    "Estado inválido: '" + estado + "'. Los valores permitidos son: " + ESTADOS_VALIDOS);
        }
    }
}
