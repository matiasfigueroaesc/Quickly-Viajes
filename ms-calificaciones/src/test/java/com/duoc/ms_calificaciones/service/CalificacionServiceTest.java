package com.duoc.ms_calificaciones.service;

import com.duoc.ms_calificaciones.exception.CalificacionNotFoundException;
import com.duoc.ms_calificaciones.model.dto.CalificacionDTO;
import com.duoc.ms_calificaciones.model.entity.Calificacion;
import com.duoc.ms_calificaciones.repository.CalificacionRepository;
import net.datafaker.Faker;
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
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository repository;

    @InjectMocks
    private CalificacionService service;

    private final Faker faker = new Faker();

    private CalificacionDTO dto;
    private Calificacion entidad;

    @BeforeEach
    void setUp() {
        dto = new CalificacionDTO();
        dto.setViajeId(1L);
        dto.setPasajeroId(2L);
        dto.setConductorId(3L);
        dto.setPuntaje(5);
        dto.setComentario(faker.lorem().sentence());

        entidad = new Calificacion();
        entidad.setId(1L);
        entidad.setViajeId(1L);
        entidad.setPasajeroId(2L);
        entidad.setConductorId(3L);
        entidad.setPuntaje(5);
        entidad.setComentario(dto.getComentario());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(CalificacionNotFoundException.class);
    }

    @Test
    void obtenerTodas_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodas()).hasSize(1);
    }

    @Test
    void guardar_nueva_guardaCorrectamente() {
        when(repository.existsByViajeIdAndPasajeroId(1L, 2L)).thenReturn(false);
        when(repository.save(any(Calificacion.class))).thenReturn(entidad);

        assertThat(service.guardar(dto).getId()).isEqualTo(1L);
    }

    @Test
    void guardar_dobleCalificacionMismoViaje_lanzaIllegalArgumentException() {
        when(repository.existsByViajeIdAndPasajeroId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya calificó");

        verify(repository, never()).save(any());
    }

    @Test
    void obtenerPromedioConductor_conDatos_retornaPromedio() {
        when(repository.promedioCalificacionConductor(3L)).thenReturn(4.5);

        assertThat(service.obtenerPromedioConductor(3L)).isEqualTo(4.5);
    }

    @Test
    void obtenerPromedioConductor_sinCalificaciones_retornaCero() {
        when(repository.promedioCalificacionConductor(3L)).thenReturn(null);

        assertThat(service.obtenerPromedioConductor(3L)).isEqualTo(0.0);
    }

    @Test
    void obtenerPorConductor_retornaListaFiltrada() {
        when(repository.findByConductorId(3L)).thenReturn(List.of(entidad));

        assertThat(service.obtenerPorConductor(3L)).hasSize(1);
    }

    @Test
    void obtenerPorPasajero_retornaListaFiltrada() {
        when(repository.findByPasajeroId(2L)).thenReturn(List.of(entidad));

        assertThat(service.obtenerPorPasajero(2L)).hasSize(1);
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Calificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setPuntaje(3);
        Calificacion resultado = service.actualizar(1L, dto);

        assertThat(resultado.getPuntaje()).isEqualTo(3);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(5L))
                .isInstanceOf(CalificacionNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}
