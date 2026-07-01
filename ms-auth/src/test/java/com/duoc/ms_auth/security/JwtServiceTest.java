package com.duoc.ms_auth.security;

import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_MS = 86_400_000L; // 24 horas

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", EXPIRATION_MS);
    }

    @Test
    void generarToken_creaTokenValido() {
        String token = jwtService.generarToken("jperez", "PASAJERO");

        assertThat(token).isNotBlank();
        assertThat(jwtService.esTokenValido(token)).isTrue();
    }

    @Test
    void extraerUsername_retornaElUsernameDelToken() {
        String token = jwtService.generarToken("jperez", "PASAJERO");

        assertThat(jwtService.extraerUsername(token)).isEqualTo("jperez");
    }

    @Test
    void extraerRol_retornaElRolDelToken() {
        String token = jwtService.generarToken("mconductor", "CONDUCTOR");

        assertThat(jwtService.extraerRol(token)).isEqualTo("CONDUCTOR");
    }

    @Test
    void esTokenValido_tokenExpirado_retornaFalse() {
        // expiration negativa -> el token ya nace expirado
        ReflectionTestUtils.setField(jwtService, "expirationMs", -10_000L);
        String tokenExpirado = jwtService.generarToken("jperez", "PASAJERO");

        assertThat(jwtService.esTokenValido(tokenExpirado)).isFalse();
    }

    @Test
    void esTokenValido_firmaAlterada_retornaFalse() {
        String token = jwtService.generarToken("jperez", "PASAJERO");
        // Se corrompe el ultimo caracter de la firma para invalidarla
        String tokenAlterado = token.substring(0, token.length() - 2) + "xx";

        assertThat(jwtService.esTokenValido(tokenAlterado)).isFalse();
    }

    @Test
    void esTokenValido_tokenMalformado_retornaFalse() {
        assertThat(jwtService.esTokenValido("esto-no-es-un-jwt-valido")).isFalse();
    }

    @Test
    void esTokenValido_firmadoConOtraClaveSecreta_retornaFalse() {
        String token = jwtService.generarToken("jperez", "PASAJERO");

        // Se valida el mismo token con una instancia de JwtService que usa otro secret
        JwtService otroServicio = new JwtService();
        String otraClave = Base64.getEncoder().encodeToString(
                Keys.hmacShaKeyFor(("otra-clave-secreta-completamente-distinta-0123456789").getBytes()).getEncoded());
        ReflectionTestUtils.setField(otroServicio, "secret", otraClave);
        ReflectionTestUtils.setField(otroServicio, "expirationMs", EXPIRATION_MS);

        assertThat(otroServicio.esTokenValido(token)).isFalse();
    }

    @Test
    void getExpirationMs_retornaElValorConfigurado() {
        assertThat(jwtService.getExpirationMs()).isEqualTo(EXPIRATION_MS);
    }

    @Test
    void generarToken_dosTokensDeUsuariosDistintos_sonDiferentes() {
        String token1 = jwtService.generarToken("usuario1", "PASAJERO");
        String token2 = jwtService.generarToken("usuario2", "CONDUCTOR");

        assertThat(token1).isNotEqualTo(token2);
    }
}
