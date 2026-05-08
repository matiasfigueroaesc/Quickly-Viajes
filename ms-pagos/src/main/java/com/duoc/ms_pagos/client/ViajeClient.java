package com.duoc.ms_pagos.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Componente que encapsula la comunicación con ms-viajes vía WebClient.
 * Se utiliza para validar que el viaje existe antes de registrar un pago.
 */
@Component
public class ViajeClient {

    private static final Logger log = LoggerFactory.getLogger(ViajeClient.class);

    private final WebClient webClientViajes;

    public ViajeClient(@Qualifier("webClientViajes") WebClient webClientViajes) {
        this.webClientViajes = webClientViajes;
    }

    /**
     * Consulta los datos del viaje en ms-viajes.
     * Lanza WebClientResponseException (404) si el viaje no existe,
     * lo cual es capturado por GlobalExceptionHandler.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerViaje(Long viajeId) {
        log.info("Consultando ms-viajes para viajeId: {}", viajeId);
        Map<String, Object> resultado = webClientViajes.get()
                .uri("/api/viajes/{id}", viajeId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        log.info("Datos del viaje {} obtenidos correctamente desde ms-viajes", viajeId);
        return resultado;
    }
}
