package com.duoc.ms_auth.service;

import com.duoc.ms_auth.exception.CredencialesInvalidasException;
import com.duoc.ms_auth.exception.UsuarioNotFoundException;
import com.duoc.ms_auth.exception.UsuarioYaExisteException;
import com.duoc.ms_auth.model.dto.AuthResponseDTO;
import com.duoc.ms_auth.model.dto.LoginDTO;
import com.duoc.ms_auth.model.dto.RegistroDTO;
import com.duoc.ms_auth.model.dto.UsuarioResponseDTO;
import com.duoc.ms_auth.model.entity.Usuario;
import com.duoc.ms_auth.repository.UsuarioRepository;
import com.duoc.ms_auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final String ROL_POR_DEFECTO = "PASAJERO";

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioResponseDTO registrar(RegistroDTO dto) {
        log.info("Intentando registrar usuario: {}", dto.getUsername());

        if (repository.existsByUsername(dto.getUsername())) {
            log.warn("Username ya en uso: {}", dto.getUsername());
            throw new UsuarioYaExisteException("Ya existe un usuario con el username: " + dto.getUsername());
        }
        if (repository.existsByEmail(dto.getEmail())) {
            log.warn("Email ya en uso: {}", dto.getEmail());
            throw new UsuarioYaExisteException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        // Nunca se guarda el password en texto plano, solo su hash BCrypt
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol((dto.getRol() == null || dto.getRol().isBlank()) ? ROL_POR_DEFECTO : dto.getRol().toUpperCase());

        Usuario guardado = repository.save(usuario);
        log.info("Usuario registrado correctamente con id: {}", guardado.getId());

        return new UsuarioResponseDTO(guardado.getId(), guardado.getUsername(), guardado.getEmail(), guardado.getRol());
    }

    public AuthResponseDTO login(LoginDTO dto) {
        log.info("Intento de login para username: {}", dto.getUsername());

        Usuario usuario = repository.findByUsername(dto.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login fallido: username no existe: {}", dto.getUsername());
                    return new CredencialesInvalidasException();
                });

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            log.warn("Login fallido: password incorrecto para username: {}", dto.getUsername());
            throw new CredencialesInvalidasException();
        }

        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol());
        log.info("Login exitoso para username: {}", dto.getUsername());

        return new AuthResponseDTO(
                token,
                "Bearer",
                usuario.getUsername(),
                usuario.getRol(),
                jwtService.getExpirationMs()
        );
    }

    public UsuarioResponseDTO obtenerPorUsername(String username) {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNotFoundException(username));
        return new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getRol());
    }

}
