package com.duoc.ms_pagos.client;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ViajeClientTest {

    @Test
    void obtenerViaje_respuestaExitosa_retornaMap() {
        ViajeClient client = crearCliente(HttpStatus.OK, "{\"id\":1,\"estado\":\"COMPLETADO\"}");

        Map<String, Object> resultado = client.obtenerViaje(1L);

        assertThat(resultado.get("id")).isEqualTo(1);
        assertThat(resultado.get("estado")).isEqualTo("COMPLETADO");
    }

    @Test
    void obtenerViaje_respuestaError_lanzaWebClientResponseException() {
        ViajeClient client = crearCliente(HttpStatus.INTERNAL_SERVER_ERROR, "{\"mensaje\":\"Falla\"}");

        assertThatThrownBy(() -> client.obtenerViaje(7L))
                .isInstanceOf(WebClientResponseException.class)
                .hasMessageContaining("500");
    }

    private ViajeClient crearCliente(HttpStatus status, String body) {
        ExchangeFunction exchangeFunction = request -> {
            ClientResponse response = ClientResponse.create(status)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .build();
            return reactor.core.publisher.Mono.just(response);
        };

        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        return new ViajeClient(webClient);
    }
}