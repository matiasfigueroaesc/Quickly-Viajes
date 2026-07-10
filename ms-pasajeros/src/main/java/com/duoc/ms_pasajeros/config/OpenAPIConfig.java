package com.duoc.ms_pasajeros.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.Paths;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "MS-Pasajeros API",
        version = "1.0.0",
        description = "Microservicio de gestión de pasajeros - QuicklyViajes",
        contact = @Contact(
            name = "Grupo 11",
            email = "grupo11@duoc.cl"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:18080",
            description = "API Gateway (Desarrollo)"
        ),
        @Server(
            url = "http://localhost:8081",
            description = "MS-Pasajeros Directo (Desarrollo)"
        )
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT obtenido del endpoint /auth/login",
    in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {

    @Bean
    public OpenApiCustomizer removeApiPrefixFromPaths() {
        return openApi -> {
            if (openApi.getPaths() == null) return;
            Paths newPaths = new Paths();
            openApi.getPaths().forEach((path, pathItem) -> {
                String newPath = path;
                if (path.startsWith("/api/")) {
                    newPath = path.substring(4);
                } else if ("/api".equals(path)) {
                    newPath = "/";
                }
                newPaths.addPathItem(newPath, pathItem);
            });
            openApi.setPaths(newPaths);
        };
    }
}
