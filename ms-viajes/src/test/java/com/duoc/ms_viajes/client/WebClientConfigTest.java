package com.duoc.ms_viajes.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

class WebClientConfigTest {

    private WebClientConfig config;

    @BeforeEach
    void setUp() {
        config = new WebClientConfig();
        ReflectionTestUtils.setField(config, "urlPasajeros", "http://pasajeros.local");
        ReflectionTestUtils.setField(config, "urlConductores", "http://conductores.local");
        ReflectionTestUtils.setField(config, "urlTarifas", "http://tarifas.local");
    }

    @Test
    void webClientPasajeros_creaClienteConBaseUrlConfigurada() {
        WebClient webClient = config.webClientPasajeros();

        assertThat(webClient).isNotNull();
    }

    @Test
    void webClientConductores_creaClienteConBaseUrlConfigurada() {
        WebClient webClient = config.webClientConductores();

        assertThat(webClient).isNotNull();
    }

    @Test
    void webClientTarifas_creaClienteConBaseUrlConfigurada() {
        WebClient webClient = config.webClientTarifas();

        assertThat(webClient).isNotNull();
    }
}