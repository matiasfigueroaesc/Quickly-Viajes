package com.duoc.ms_tarifas.service;

import com.duoc.ms_tarifas.exception.TarifaNotFoundException;
import com.duoc.ms_tarifas.model.dto.TarifaDTO;
import com.duoc.ms_tarifas.model.entity.Tarifa;
import com.duoc.ms_tarifas.repository.TarifaRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarifaServiceTest {

    @Mock
    private TarifaRepository repository;

    @InjectMocks
    private TarifaService service;

    private final Faker faker = new Faker();

    private TarifaDTO dto;
    private Tarifa entidad;

    @BeforeEach
    void setUp() {
        dto = new TarifaDTO();
        dto.setNombre("Tarifa " + faker.commerce().productName());
        dto.setPrecioBase(new BigDecimal("3500.00"));
        dto.setDescripcion(faker.lorem().sentence());

        entidad = new Tarifa();
        entidad.setId(1L);
        entidad.setNombre(dto.getNombre());
        entidad.setPrecioBase(dto.getPrecioBase());
        entidad.setDescripcion(dto.getDescripcion());
    }

    @Test
    void obtenerTodas_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodas()).hasSize(1);
    }

    @Test
    void obtenerPorId_existente_retornaTarifa() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(1L).getNombre()).isEqualTo(dto.getNombre());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(TarifaNotFoundException.class);
    }

    @Test
    void guardar_nombreNuevo_guardaCorrectamente() {
        when(repository.existsByNombre(dto.getNombre())).thenReturn(false);
        when(repository.save(any(Tarifa.class))).thenReturn(entidad);

        assertThat(service.guardar(dto).getId()).isEqualTo(1L);
    }

    @Test
    void guardar_nombreDuplicado_lanzaIllegalArgumentException() {
        when(repository.existsByNombre(dto.getNombre())).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(dto.getNombre());

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Tarifa.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setPrecioBase(new BigDecimal("4200.00"));
        Tarifa resultado = service.actualizar(1L, dto);

        assertThat(resultado.getPrecioBase()).isEqualTo(new BigDecimal("4200.00"));
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, dto))
                .isInstanceOf(TarifaNotFoundException.class);
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
                .isInstanceOf(TarifaNotFoundException.class);
    }
}
