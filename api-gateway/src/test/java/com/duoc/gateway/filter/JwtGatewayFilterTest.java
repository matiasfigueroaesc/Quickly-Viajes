package com.duoc.gateway.filter;

import com.duoc.gateway.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class JwtGatewayFilterTest {

    @Test
    void shouldAllowSwaggerUiWithoutJwt() {
        JwtGatewayFilter filter = new JwtGatewayFilter();
        JwtUtil jwtUtil = mock(JwtUtil.class);
        ReflectionTestUtils.setField(filter, "jwtUtil", jwtUtil);

        ServerHttpRequest request = MockServerHttpRequest.get("/swagger-ui.html").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = exchange1 -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        filter.filter(exchange, chain).block();

        assertThat(chainCalled).isTrue();
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void shouldAllowOpenApiDocsWithoutJwt() {
        JwtGatewayFilter filter = new JwtGatewayFilter();
        JwtUtil jwtUtil = mock(JwtUtil.class);
        ReflectionTestUtils.setField(filter, "jwtUtil", jwtUtil);

        ServerHttpRequest request = MockServerHttpRequest.get("/v3/api-docs").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        AtomicBoolean chainCalled = new AtomicBoolean(false);
        GatewayFilterChain chain = exchange1 -> {
            chainCalled.set(true);
            return Mono.empty();
        };

        filter.filter(exchange, chain).block();

        assertThat(chainCalled).isTrue();
        verifyNoInteractions(jwtUtil);
    }
}
