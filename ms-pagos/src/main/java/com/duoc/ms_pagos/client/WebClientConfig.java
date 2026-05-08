package com.duoc.ms_pagos.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuración de WebClient para consumir ms-viajes.
 * La URL se define en application.yml bajo la clave "microservicios.viajes.url".
 */
@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Value("${microservicios.viajes.url}")
    private String urlViajes;

    @Bean(name = "webClientViajes")
    public WebClient webClientViajes() {
        log.info("Configurando WebClient para ms-viajes en: {}", urlViajes);
        return WebClient.builder()
                .baseUrl(urlViajes)
                .build();
    }
}
