package com.duoc.ms_notificaciones.service;

import com.duoc.ms_notificaciones.exception.NotificacionNotFoundException;
import com.duoc.ms_notificaciones.model.dto.NotificacionDTO;
import com.duoc.ms_notificaciones.model.entity.Notificacion;
import com.duoc.ms_notificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionService service;

    private NotificacionDTO dto;
    private Notificacion entidad;

    @BeforeEach
    void setUp() {
        dto = new NotificacionDTO();
        dto.setViajeId(1L);
        dto.setTipo("INICIO_VIAJE");
        dto.setMensaje("Tu viaje ha comenzado");
        dto.setCanal("PUSH");

        entidad = new Notificacion();
        entidad.setId(1L);
        entidad.setViajeId(1L);
        entidad.setTipo("INICIO_VIAJE");
        entidad.setMensaje(dto.getMensaje());
        entidad.setCanal("PUSH");
        entidad.setEstado("ENVIADA");
    }

    @Test
    void enviar_creaNotificacionConEstadoEnviada() {
        when(repository.save(any(Notificacion.class))).thenReturn(entidad);

        Notificacion resultado = service.enviar(dto);

        assertThat(resultado.getEstado()).isEqualTo("ENVIADA");
        assertThat(resultado.getViajeId()).isEqualTo(1L);
    }

    @Test
    void obtenerTodas_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodas()).hasSize(1);
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(NotificacionNotFoundException.class);
    }

    @Test
    void obtenerPorViajeId_retornaNotificacionesDelViaje() {
        when(repository.findByViajeId(1L)).thenReturn(List.of(entidad));

        assertThat(service.obtenerPorViajeId(1L)).hasSize(1);
    }

    @Test
    void obtenerPorEstado_normalizaAMayusculas() {
        when(repository.findByEstado("ENVIADA")).thenReturn(List.of(entidad));

        List<Notificacion> resultado = service.obtenerPorEstado("enviada");

        assertThat(resultado).hasSize(1);
        verify(repository).findByEstado("ENVIADA");
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setMensaje("Mensaje actualizado");
        Notificacion resultado = service.actualizar(1L, dto);

        assertThat(resultado.getMensaje()).isEqualTo("Mensaje actualizado");
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, dto))
                .isInstanceOf(NotificacionNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(5L))
                .isInstanceOf(NotificacionNotFoundException.class);
    }
}
