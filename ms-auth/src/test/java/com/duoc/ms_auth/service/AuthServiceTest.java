package com.duoc.ms_auth.service;

import com.duoc.ms_auth.exception.CredencialesInvalidasException;
import com.duoc.ms_auth.exception.UsuarioYaExisteException;
import com.duoc.ms_auth.model.dto.AuthResponseDTO;
import com.duoc.ms_auth.model.dto.LoginDTO;
import com.duoc.ms_auth.model.dto.RegistroDTO;
import com.duoc.ms_auth.model.dto.UsuarioResponseDTO;
import com.duoc.ms_auth.model.entity.Usuario;
import com.duoc.ms_auth.repository.UsuarioRepository;
import com.duoc.ms_auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    private RegistroDTO registroDTO;
    private LoginDTO loginDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        registroDTO = new RegistroDTO();
        registroDTO.setUsername("jperez");
        registroDTO.setEmail("jperez@correo.com");
        registroDTO.setPassword("clave123");

        loginDTO = new LoginDTO();
        loginDTO.setUsername("jperez");
        loginDTO.setPassword("clave123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("jperez");
        usuario.setEmail("jperez@correo.com");
        usuario.setPassword("$2a$10$hashSimuladoBCrypt");
        usuario.setRol("PASAJERO");
    }

    @Test
    void registrar_sinRolEspecificado_asignaPasajeroPorDefecto() {
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("$2a$10$hashSimuladoBCrypt");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO resultado = service.registrar(registroDTO);

        assertThat(resultado.getRol()).isEqualTo("PASAJERO");
        assertThat(resultado.getUsername()).isEqualTo("jperez");
    }

    @Test
    void registrar_passwordNuncaSeGuardaEnTextoPlano() {
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("clave123")).thenReturn("$2a$10$hashSimuladoBCrypt");
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        service.registrar(registroDTO);

        // Verifica que SIEMPRE se llama al encoder antes de guardar (nunca texto plano)
        verify(passwordEncoder, times(1)).encode("clave123");
        verify(repository).save(argThat(u -> u.getPassword().equals("$2a$10$hashSimuladoBCrypt")
                && !u.getPassword().equals("clave123")));
    }

    @Test
    void registrar_usernameDuplicado_lanzaUsuarioYaExisteException() {
        when(repository.existsByUsername("jperez")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(registroDTO))
                .isInstanceOf(UsuarioYaExisteException.class)
                .hasMessageContaining("jperez");

        verify(repository, never()).save(any());
    }

    @Test
    void registrar_emailDuplicado_lanzaUsuarioYaExisteException() {
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@correo.com")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(registroDTO))
                .isInstanceOf(UsuarioYaExisteException.class)
                .hasMessageContaining("jperez@correo.com");

        verify(repository, never()).save(any());
    }

    @Test
    void registrar_conRolExplicito_loNormalizaAMayusculas() {
        registroDTO.setRol("conductor");
        when(repository.existsByUsername("jperez")).thenReturn(false);
        when(repository.existsByEmail("jperez@correo.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("hash");
        when(repository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponseDTO resultado = service.registrar(registroDTO);

        assertThat(resultado.getRol()).isEqualTo("CONDUCTOR");
    }

    @Test
    void login_credencialesValidas_retornaTokenYDatos() {
        when(repository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave123", usuario.getPassword())).thenReturn(true);
        when(jwtService.generarToken("jperez", "PASAJERO")).thenReturn("token.jwt.simulado");
        when(jwtService.getExpirationMs()).thenReturn(86400000L);

        AuthResponseDTO resultado = service.login(loginDTO);

        assertThat(resultado.getToken()).isEqualTo("token.jwt.simulado");
        assertThat(resultado.getTipo()).isEqualTo("Bearer");
        assertThat(resultado.getRol()).isEqualTo("PASAJERO");
    }

    @Test
    void login_usernameInexistente_lanzaCredencialesInvalidas() {
        when(repository.findByUsername("noexiste")).thenReturn(Optional.empty());
        loginDTO.setUsername("noexiste");

        assertThatThrownBy(() -> service.login(loginDTO))
                .isInstanceOf(CredencialesInvalidasException.class);

        verifyNoInteractions(jwtService);
    }

    @Test
    void login_passwordIncorrecto_lanzaCredencialesInvalidas() {
        when(repository.findByUsername("jperez")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("claveMala", usuario.getPassword())).thenReturn(false);
        loginDTO.setPassword("claveMala");

        assertThatThrownBy(() -> service.login(loginDTO))
                .isInstanceOf(CredencialesInvalidasException.class);

        verifyNoInteractions(jwtService);
    }
}
