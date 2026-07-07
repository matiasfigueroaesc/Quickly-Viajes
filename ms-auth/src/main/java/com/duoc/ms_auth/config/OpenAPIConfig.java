package com.duoc.ms_auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "MS-Autenticación API",
        version = "1.0.0",
        description = "Gestión de usuarios, login y tokens JWT - QuicklyViajes",
        contact = @Contact(
            name = "Grupo 11",
            email = "grupo11@duoc.cl"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "API Gateway (Desarrollo)"
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
}
