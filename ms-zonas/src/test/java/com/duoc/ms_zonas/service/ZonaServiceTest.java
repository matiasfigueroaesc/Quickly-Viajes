package com.duoc.ms_zonas.service;

import com.duoc.ms_zonas.exception.ZonaNotFoundException;
import com.duoc.ms_zonas.model.dto.ZonaDTO;
import com.duoc.ms_zonas.model.entity.Zona;
import com.duoc.ms_zonas.repository.ZonaRepository;
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
class ZonaServiceTest {

    @Mock
    private ZonaRepository repository;

    @InjectMocks
    private ZonaService service;

    private ZonaDTO dto;
    private Zona entidad;

    @BeforeEach
    void setUp() {
        dto = new ZonaDTO();
        dto.setNombre("Zona Centro");
        dto.setDescripcion("Zona centrica de la ciudad");
        dto.setActiva(true);

        entidad = new Zona();
        entidad.setId(1L);
        entidad.setNombre(dto.getNombre());
        entidad.setDescripcion(dto.getDescripcion());
        entidad.setActiva(true);
    }

    @Test
    void obtenerActivas_retornaSoloActivas() {
        when(repository.findByActivaTrue()).thenReturn(List.of(entidad));

        assertThat(service.obtenerActivas()).hasSize(1);
    }

    @Test
    void obtenerTodas_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodas()).hasSize(1);
    }

    @Test
    void obtenerPorId_existente_retornaZona() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(1L).getNombre()).isEqualTo(dto.getNombre());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ZonaNotFoundException.class);
    }

    @Test
    void guardar_nombreNuevo_guardaCorrectamente() {
        when(repository.existsByNombre(dto.getNombre())).thenReturn(false);
        when(repository.save(any(Zona.class))).thenReturn(entidad);

        assertThat(service.guardar(dto).getId()).isEqualTo(1L);
    }

    @Test
    void guardar_nombreDuplicado_lanzaIllegalArgumentException() {
        when(repository.existsByNombre(dto.getNombre())).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_mismoNombre_noValidaDuplicadoContraSiMisma() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Zona.class))).thenAnswer(inv -> inv.getArgument(0));

        // dto.getNombre() es igual al de la entidad -> no debe llamar existsByNombre
        Zona resultado = service.actualizar(1L, dto);

        assertThat(resultado.getNombre()).isEqualTo("Zona Centro");
        verify(repository, never()).existsByNombre(any());
    }

    @Test
    void actualizar_nombreCambiadoYaExistente_lanzaIllegalArgumentException() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.existsByNombre("Zona Nueva")).thenReturn(true);

        dto.setNombre("Zona Nueva");

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(50L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(50L))
                .isInstanceOf(ZonaNotFoundException.class);
    }
}
