package com.duoc.ms_viajes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClient para consumir ms-pasajeros, ms-conductores y ms-tarifas.
 * Las URLs se definen en application.yml bajo la clave "microservicios".
 */
@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${microservicios.pasajeros.url}")
    private String urlPasajeros;

    @Value("${microservicios.conductores.url}")
    private String urlConductores;

    @Value("${microservicios.tarifas.url}")
    private String urlTarifas;

    @Bean(name = "webClientPasajeros")
    public WebClient webClientPasajeros() {
        log.info("Configurando WebClient para ms-pasajeros en: {}", urlPasajeros);
        return WebClient.builder()
                .baseUrl(urlPasajeros)
                .build();
    }

    @Bean(name = "webClientConductores")
    public WebClient webClientConductores() {
        log.info("Configurando WebClient para ms-conductores en: {}", urlConductores);
        return WebClient.builder()
                .baseUrl(urlConductores)
                .build();
    }

    @Bean(name = "webClientTarifas")
    public WebClient webClientTarifas() {
        log.info("Configurando WebClient para ms-tarifas en: {}", urlTarifas);
        return WebClient.builder()
                .baseUrl(urlTarifas)
                .build();
    }
}
