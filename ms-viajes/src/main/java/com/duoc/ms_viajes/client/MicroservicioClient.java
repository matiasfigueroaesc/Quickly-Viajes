package com.duoc.ms_viajes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Componente que centraliza todas las llamadas a microservicios externos.
 * Utiliza WebClient para comunicación HTTP sincrónica (bloqueo con .block()).
 * Maneja timeouts y errores de red de forma controlada.
 */
@Component
public class MicroservicioClient {

    private static final Logger log = LoggerFactory.getLogger(MicroservicioClient.class);

    private final WebClient webClientPasajeros;
    private final WebClient webClientConductores;
    private final WebClient webClientTarifas;

    public MicroservicioClient(
            @Qualifier("webClientPasajeros") WebClient webClientPasajeros,
            @Qualifier("webClientConductores") WebClient webClientConductores,
            @Qualifier("webClientTarifas") WebClient webClientTarifas) {
        this.webClientPasajeros = webClientPasajeros;
        this.webClientConductores = webClientConductores;
        this.webClientTarifas = webClientTarifas;
    }

    /**
     * Consulta los datos del pasajero en ms-pasajeros.
     * Retorna un Map con los campos del pasajero o lanza excepción si no existe.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerPasajero(Long pasajeroId) {
        log.info("Consultando ms-pasajeros para pasajeroId: {}", pasajeroId);
        Map<String, Object> resultado = webClientPasajeros.get()
                .uri("/api/pasajeros/{id}", pasajeroId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        log.info("Datos de pasajero {} obtenidos correctamente", pasajeroId);
        return resultado;
    }

    /**
     * Consulta los datos del conductor en ms-conductores.
     * Retorna un Map con los campos del conductor o lanza excepción si no existe.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerConductor(Long conductorId) {
        log.info("Consultando ms-conductores para conductorId: {}", conductorId);
        Map<String, Object> resultado = webClientConductores.get()
                .uri("/api/conductores/{id}", conductorId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        log.info("Datos de conductor {} obtenidos correctamente", conductorId);
        return resultado;
    }

    /**
     * Consulta los datos de la tarifa en ms-tarifas.
     * Retorna un Map con los campos de la tarifa o lanza excepción si no existe.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> obtenerTarifa(Long tarifaId) {
        log.info("Consultando ms-tarifas para tarifaId: {}", tarifaId);
        Map<String, Object> resultado = webClientTarifas.get()
                .uri("/api/tarifas/{id}", tarifaId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        log.info("Datos de tarifa {} obtenidos correctamente", tarifaId);
        return resultado;
    }
}
