package com.duoc.ms_pagos.service;

import com.duoc.ms_pagos.client.ViajeClient;
import com.duoc.ms_pagos.exception.PagoNotFoundException;
import com.duoc.ms_pagos.model.dto.PagoDTO;
import com.duoc.ms_pagos.model.entity.Pago;
import com.duoc.ms_pagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository repository;

    @Mock
    private ViajeClient viajeClient;

    @InjectMocks
    private PagoService service;

    private PagoDTO dto;
    private Pago entidad;

    @BeforeEach
    void setUp() {
        dto = new PagoDTO();
        dto.setViajeId(1L);
        dto.setMonto(new BigDecimal("5000.00"));
        dto.setMetodoPago("TARJETA_CREDITO");
        dto.setEstado("COMPLETADO");
        dto.setFechaPago(LocalDateTime.now());

        entidad = new Pago();
        entidad.setId(1L);
        entidad.setViajeId(1L);
        entidad.setMonto(dto.getMonto());
        entidad.setMetodoPago(dto.getMetodoPago());
        entidad.setEstado(dto.getEstado());
        entidad.setFechaPago(dto.getFechaPago());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(PagoNotFoundException.class);
    }

    @Test
    void obtenerPorId_existente_retornaPago() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(1L).getId()).isEqualTo(1L);
    }

    @Test
    void obtenerTodos_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerTodos()).hasSize(1);
    }

    @Test
    void obtenerPorViaje_retornaPagosDelViaje() {
        when(repository.findByViajeId(1L)).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerPorViaje(1L)).hasSize(1);
    }

    @Test
    void obtenerPorEstado_valido_retornaPagosFiltrados() {
        when(repository.findByEstado("COMPLETADO")).thenReturn(java.util.List.of(entidad));

        assertThat(service.obtenerPorEstado("COMPLETADO")).hasSize(1);
    }

    @Test
    void guardar_metodoPagoInvalido_lanzaIllegalArgumentException() {
        dto.setMetodoPago("CHEQUE");

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Método de pago inválido");

        verify(repository, never()).save(any());
        verifyNoInteractions(viajeClient);
    }

    @Test
    void guardar_estadoInvalido_lanzaIllegalArgumentException() {
        dto.setEstado("EN_PROCESO");

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Estado inválido");
    }

    @Test
    void guardar_viajeNoPagable_lanzaIllegalArgumentException() {
        when(viajeClient.obtenerViaje(1L)).thenReturn(Map.of("estado", "PENDIENTE"));

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PENDIENTE");

        verify(repository, never()).save(any());
    }

    @Test
    void guardar_dobleCompletadoParaMismoViaje_lanzaIllegalArgumentException() {
        when(viajeClient.obtenerViaje(1L)).thenReturn(Map.of("estado", "COMPLETADO"));
        when(repository.existsByViajeIdAndEstado(1L, "COMPLETADO")).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya cuenta con un pago completado");

        verify(repository, never()).save(any());
    }

    @Test
    void guardar_datosValidos_guardaCorrectamente() {
        when(viajeClient.obtenerViaje(1L)).thenReturn(Map.of("estado", "COMPLETADO"));
        when(repository.existsByViajeIdAndEstado(1L, "COMPLETADO")).thenReturn(false);
        when(repository.save(any(Pago.class))).thenReturn(entidad);

        Pago resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void actualizar_pagoCompletado_lanzaIllegalArgumentException() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("COMPLETADO");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_pagoReembolsado_lanzaIllegalArgumentException() {
        entidad.setEstado("REEMBOLSADO");
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("REEMBOLSADO");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_pagoExistente_yDatosValidos_actualizaCorrectamente() {
        entidad.setEstado("PENDIENTE");
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Pago.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setEstado("PENDIENTE");

        Pago resultado = service.actualizar(1L, dto);

        assertThat(resultado.getEstado()).isEqualTo("PENDIENTE");
    }

    @Test
    void actualizar_pagoInexistente_lanzaNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(1L, dto))
                .isInstanceOf(PagoNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(1L))
                .isInstanceOf(PagoNotFoundException.class);
    }

    @Test
    void obtenerPorEstado_estadoInvalido_lanzaIllegalArgumentException() {
        assertThatThrownBy(() -> service.obtenerPorEstado("INVALIDO"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
