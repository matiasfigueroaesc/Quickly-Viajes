package com.duoc.ms_auth.controller;

import com.duoc.ms_auth.model.dto.AuthResponseDTO;
import com.duoc.ms_auth.model.dto.LoginDTO;
import com.duoc.ms_auth.model.dto.RegistroDTO;
import com.duoc.ms_auth.model.dto.UsuarioResponseDTO;
import com.duoc.ms_auth.service.AuthService;
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
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService service;

    // POST /auth/register → crea un nuevo usuario con password hasheado en BCrypt
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody RegistroDTO dto) {
        log.info("POST /auth/register - registrando usuario: {}", dto.getUsername());
        UsuarioResponseDTO nuevo = service.registrar(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // POST /auth/login → valida credenciales y devuelve un JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        log.info("POST /auth/login - intento de login: {}", dto.getUsername());
        AuthResponseDTO respuesta = service.login(dto);
        return ResponseEntity.ok(respuesta);
    }

    // GET /auth/me → endpoint protegido de ejemplo: requiere un JWT valido
    // (Authorization: Bearer <token>). Sirve para probar que la emision y
    // validacion del token funcionan de extremo a extremo, sin depender
    // todavia del API Gateway (que se construye en FASE 2).
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioActual(Authentication authentication) {
        String username = authentication.getName();
        log.info("GET /auth/me - usuario autenticado: {}", username);
        return ResponseEntity.ok(service.obtenerPorUsername(username));
    }

}
