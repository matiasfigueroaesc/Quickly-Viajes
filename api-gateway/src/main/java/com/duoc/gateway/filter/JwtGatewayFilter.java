package com.duoc.gateway.filter;

import com.duoc.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // Rutas públicas que NO requieren token JWT
    private static final List<String> PUBLIC_ROUTES = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/h2-console",
            "/actuator",
            "/actuator/health",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/docs",
            "/docs/**",
            "/webjars/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Si es una ruta pública, continuar sin validación
        if (isPublicRoute(path)) {
            log.info("Acceso a ruta pública: {}", path);
            return chain.filter(exchange);
        }

        // Extraer el token del header Authorization
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Token JWT faltante o formato incorrecto para ruta: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring("Bearer ".length());

        // Validar el token
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Token JWT inválido o expirado para ruta: {}", path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Extraer información del token y agregarla como headers para los microservicios
        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        log.info("Token validado para usuario: {} con rol: {} en ruta: {}", username, role, path);

        // Modificar el request para agregar headers con la información del usuario
        // Los microservicios pueden confiar en estos headers (ya fueron validados por el Gateway)
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Role", role != null ? role : "PASAJERO")
                        .build())
                .build();

        return chain.filter(modifiedExchange);
    }

    private final org.springframework.util.AntPathMatcher pathMatcher = new org.springframework.util.AntPathMatcher();

    /**
     * Verifica si la ruta es pública (no requiere autenticación).
     */
    private boolean isPublicRoute(String path) {
        return PUBLIC_ROUTES.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    /**
     * Orden de ejecución del filtro (más bajo = se ejecuta primero).
     */
    @Override
    public int getOrder() {
        return -1; // Se ejecuta antes que otros filtros
    }
}
