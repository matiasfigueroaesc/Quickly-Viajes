package com.duoc.ms_incidencias.service;

import com.duoc.ms_incidencias.exception.IncidenciaNotFoundException;
import com.duoc.ms_incidencias.model.dto.EstadoIncidenciaDTO;
import com.duoc.ms_incidencias.model.dto.IncidenciaDTO;
import com.duoc.ms_incidencias.model.entity.Incidencia;
import com.duoc.ms_incidencias.repository.IncidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceTest {

    @Mock
    private IncidenciaRepository repository;

    @InjectMocks
    private IncidenciaService service;

    private IncidenciaDTO dto;
    private Incidencia entidad;

    @BeforeEach
    void setUp() {
        dto = new IncidenciaDTO();
        dto.setViajeId(1L);
        dto.setReportadoPor(2L);
        dto.setTipo("ACCIDENTE");
        dto.setDescripcion("Descripcion de prueba con suficiente longitud");

        entidad = new Incidencia();
        entidad.setId(1L);
        entidad.setViajeId(1L);
        entidad.setReportadoPor(2L);
        entidad.setTipo("ACCIDENTE");
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setEstado("ABIERTA");
    }

    @Test
    void guardar_creaIncidenciaEnEstadoAbierta() {
        when(repository.save(any(Incidencia.class))).thenReturn(entidad);

        Incidencia resultado = service.guardar(dto);

        assertThat(resultado.getEstado()).isEqualTo("ABIERTA");
    }

    @Test
    void obtenerTodas_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerTodas()).hasSize(1);
    }

    @Test
    void obtenerPorViaje_retornaListaFiltrada() {
        when(repository.findByViajeId(1L)).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerPorViaje(1L)).hasSize(1);
    }

    @Test
    void obtenerPorEstado_retornaListaFiltrada() {
        when(repository.findByEstado("ABIERTA")).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerPorEstado("ABIERTA")).hasSize(1);
    }

    @Test
    void obtenerPorReportante_retornaListaFiltrada() {
        when(repository.findByReportadoPor(2L)).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerPorReportante(2L)).hasSize(1);
    }

    @Test
    void actualizarEstado_transicionARresuelta_asignaFechaResolucion() {
        EstadoIncidenciaDTO estadoDTO = new EstadoIncidenciaDTO();
        estadoDTO.setEstado("RESUELTA");

        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Incidencia.class))).thenAnswer(inv -> inv.getArgument(0));

        Incidencia resultado = service.actualizarEstado(1L, estadoDTO);

        assertThat(resultado.getEstado()).isEqualTo("RESUELTA");
        assertThat(resultado.getFechaResolucion()).isNotNull();
    }

    @Test
    void actualizarEstado_incidenciaCerrada_lanzaIllegalArgumentException() {
        entidad.setEstado("CERRADA");
        EstadoIncidenciaDTO estadoDTO = new EstadoIncidenciaDTO();
        estadoDTO.setEstado("EN_REVISION");

        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThatThrownBy(() -> service.actualizarEstado(1L, estadoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CERRADA");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_incidenciaResuelta_lanzaIllegalArgumentException() {
        entidad.setEstado("RESUELTA");
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RESUELTA");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_incidenciaAbierta_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Incidencia.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setDescripcion("Nueva descripcion actualizada con longitud valida");
        Incidencia resultado = service.actualizar(1L, dto);

        assertThat(resultado.getDescripcion()).isEqualTo("Nueva descripcion actualizada con longitud valida");
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(IncidenciaNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }
}
