package com.duoc.ms_viajes.client;

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

class MicroservicioClientTest {

    @Test
    void obtenerPasajero_respuestaExitosa_retornaMap() {
        MicroservicioClient client = crearCliente(HttpStatus.OK, "{\"id\":1,\"nombre\":\"Juan Perez\"}");

        Map<String, Object> resultado = client.obtenerPasajero(1L);

        assertThat(resultado.get("id")).isEqualTo(1);
        assertThat(resultado.get("nombre")).isEqualTo("Juan Perez");
    }

    @Test
    void obtenerConductor_respuestaExitosa_retornaMap() {
        MicroservicioClient client = crearCliente(HttpStatus.OK, "{\"id\":2,\"nombre\":\"Pedro Soto\"}");

        Map<String, Object> resultado = client.obtenerConductor(2L);

        assertThat(resultado.get("id")).isEqualTo(2);
        assertThat(resultado.get("nombre")).isEqualTo("Pedro Soto");
    }

    @Test
    void obtenerTarifa_respuestaExitosa_retornaMap() {
        MicroservicioClient client = crearCliente(HttpStatus.OK, "{\"id\":3,\"nombre\":\"Tarifa Base\"}");

        Map<String, Object> resultado = client.obtenerTarifa(3L);

        assertThat(resultado.get("id")).isEqualTo(3);
        assertThat(resultado.get("nombre")).isEqualTo("Tarifa Base");
    }

    @Test
    void obtenerPasajero_respuesta404_lanzaWebClientResponseException() {
        MicroservicioClient client = crearCliente(HttpStatus.NOT_FOUND, "{\"mensaje\":\"No encontrado\"}");

        assertThatThrownBy(() -> client.obtenerPasajero(99L))
                .isInstanceOf(WebClientResponseException.class)
                .hasMessageContaining("404");
    }

    private MicroservicioClient crearCliente(HttpStatus status, String body) {
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

        return new MicroservicioClient(webClient, webClient, webClient);
    }
}