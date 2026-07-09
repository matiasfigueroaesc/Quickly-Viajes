package com.duoc.ms_vehiculos.service;

import com.duoc.ms_vehiculos.exception.VehiculoNotFoundException;
import com.duoc.ms_vehiculos.model.dto.VehiculoDTO;
import com.duoc.ms_vehiculos.model.entity.Vehiculo;
import com.duoc.ms_vehiculos.repository.VehiculoRepository;
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
class VehiculoServiceTest {

    @Mock
    private VehiculoRepository repository;

    @InjectMocks
    private VehiculoService service;

    private VehiculoDTO dto;
    private Vehiculo entidad;

    @BeforeEach
    void setUp() {
        dto = new VehiculoDTO();
        dto.setPatente("ABCD12");
        dto.setMarca("Toyota");
        dto.setModelo("Yaris");
        dto.setAnio(2022);
        dto.setTipo("SEDAN");

        entidad = new Vehiculo();
        entidad.setId(1L);
        entidad.setPatente(dto.getPatente());
        entidad.setMarca(dto.getMarca());
        entidad.setModelo(dto.getModelo());
        entidad.setAnio(dto.getAnio());
        entidad.setTipo(dto.getTipo());
    }

    @Test
    void obtenerTodos_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodos()).hasSize(1);
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(VehiculoNotFoundException.class);
    }

    @Test
    void guardar_patenteNueva_guardaCorrectamente() {
        when(repository.existsByPatente(dto.getPatente())).thenReturn(false);
        when(repository.save(any(Vehiculo.class))).thenReturn(entidad);

        assertThat(service.guardar(dto).getId()).isEqualTo(1L);
    }

    @Test
    void guardar_patenteDuplicada_lanzaIllegalArgumentException() {
        // Cubre el bug fix de FASE 5: antes retornaba 500 en vez de 409 en el controller
        when(repository.existsByPatente(dto.getPatente())).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(dto.getPatente());

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Vehiculo.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setModelo("Corolla");
        Vehiculo resultado = service.actualizar(1L, dto);

        assertThat(resultado.getModelo()).isEqualTo("Corolla");
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, dto))
                .isInstanceOf(VehiculoNotFoundException.class);
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
                .isInstanceOf(VehiculoNotFoundException.class);
    }

    @Test
    void obtenerPorId_existente_retornaVehiculo() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(1L).getPatente()).isEqualTo(dto.getPatente());
    }
}
