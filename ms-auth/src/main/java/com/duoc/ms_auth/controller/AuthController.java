package com.duoc.ms_auth.controller;

import com.duoc.ms_auth.model.dto.AuthResponseDTO;
import com.duoc.ms_auth.model.dto.LoginDTO;
import com.duoc.ms_auth.model.dto.RegistroDTO;
import com.duoc.ms_auth.model.dto.UsuarioResponseDTO;
import com.duoc.ms_auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Gestión de usuarios, login y tokens JWT")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService service;

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario en el sistema con credenciales seguras (password hasheado en BCrypt)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
    })
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroDTO dto) {
        log.info("POST /auth/register - registrando usuario: {}", dto.getUsername());
        UsuarioResponseDTO nuevo = service.registrar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login y obtener token JWT",
        description = "Valida las credenciales del usuario y devuelve un token JWT para acceder a rutas protegidas"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso, token generado"),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        log.info("POST /auth/login - intento de login: {}", dto.getUsername());
        AuthResponseDTO respuesta = service.login(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(
        summary = "Obtener usuario autenticado",
        description = "Retorna los datos del usuario autenticado (requiere token JWT válido)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente"),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    })
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /auth/me - usuario autenticado: {}", username);
        return ResponseEntity.ok(service.obtenerPorUsername(username));
    }

}
