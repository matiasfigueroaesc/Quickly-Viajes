package com.duoc.ms_viajes.service;

import com.duoc.ms_viajes.client.MicroservicioClient;
import com.duoc.ms_viajes.exception.ViajeNotFoundException;
import com.duoc.ms_viajes.model.dto.ViajeDTO;
import com.duoc.ms_viajes.model.dto.ViajeDetalleDTO;
import com.duoc.ms_viajes.model.entity.Viaje;
import com.duoc.ms_viajes.repository.ViajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViajeServiceTest {

    @Mock
    private ViajeRepository repository;

    @Mock
    private MicroservicioClient microservicioClient;

    @InjectMocks
    private ViajeService service;

    private ViajeDTO dto;
    private Viaje entidad;

    @BeforeEach
    void setUp() {
        dto = new ViajeDTO();
        dto.setPasajeroId(1L);
        dto.setConductorId(2L);
        dto.setTarifaId(3L);
        dto.setOrigen("Av. Siempre Viva 123");
        dto.setDestino("Plaza de Armas");
        dto.setEstado("PENDIENTE");
        dto.setFechaInicio(LocalDateTime.now());

        entidad = new Viaje();
        entidad.setId(10L);
        entidad.setPasajeroId(1L);
        entidad.setConductorId(2L);
        entidad.setTarifaId(3L);
        entidad.setOrigen(dto.getOrigen());
        entidad.setDestino(dto.getDestino());
        entidad.setEstado("PENDIENTE");
        entidad.setFechaInicio(dto.getFechaInicio());
    }

    @Test
    void obtenerPorId_existente_retornaViaje() {
        when(repository.findById(10L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(10L)).isEqualTo(entidad);
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(999L))
                .isInstanceOf(ViajeNotFoundException.class);
    }

    @Test
    void guardar_estadoInvalido_lanzaIllegalArgumentException() {
        dto.setEstado("VOLANDO");

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Estado inválido");

        verify(repository, never()).save(any());
    }

    @Test
    void guardar_conductorConViajeEnCurso_lanzaIllegalArgumentException() {
        dto.setEstado("EN_CURSO");
        when(repository.existsByConductorIdAndEstado(2L, "EN_CURSO")).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya tiene un viaje en curso");

        verify(repository, never()).save(any());
    }

    @Test
    void guardar_datosValidos_verificaMicroserviciosYGuarda() {
        when(microservicioClient.obtenerPasajero(1L)).thenReturn(Map.of("id", 1L, "nombre", "Juan"));
        when(microservicioClient.obtenerConductor(2L)).thenReturn(Map.of("id", 2L, "nombre", "Pedro"));
        when(microservicioClient.obtenerTarifa(3L)).thenReturn(Map.of("id", 3L, "nombre", "Tarifa Base"));
        when(repository.save(any(Viaje.class))).thenReturn(entidad);

        Viaje resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        verify(microservicioClient).obtenerPasajero(1L);
        verify(microservicioClient).obtenerConductor(2L);
        verify(microservicioClient).obtenerTarifa(3L);
    }

    @Test
    void actualizar_viajeCompletado_lanzaIllegalArgumentException() {
        entidad.setEstado("COMPLETADO");
        when(repository.findById(10L)).thenReturn(Optional.of(entidad));

        assertThatThrownBy(() -> service.actualizar(10L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("COMPLETADO");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_viajePendiente_actualizaCorrectamente() {
        when(repository.findById(10L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Viaje.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setDestino("Nuevo Destino 456");
        Viaje resultado = service.actualizar(10L, dto);

        assertThat(resultado.getDestino()).isEqualTo("Nuevo Destino 456");
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(1L))
                .isInstanceOf(ViajeNotFoundException.class);
    }

    @Test
    void obtenerDetallePorId_construyeDetalleConDatosRemotos() {
        entidad.setMontoTotal(new BigDecimal("5000.00"));
        when(repository.findById(10L)).thenReturn(Optional.of(entidad));
        when(microservicioClient.obtenerPasajero(1L))
                .thenReturn(Map.of("nombre", "Juan Perez", "email", "juan@correo.com"));
        when(microservicioClient.obtenerConductor(2L))
                .thenReturn(Map.of("nombre", "Pedro Soto", "licencia", "ABC123"));
        when(microservicioClient.obtenerTarifa(3L))
                .thenReturn(Map.of("nombre", "Tarifa Base", "precioBase", "3500.00"));

        ViajeDetalleDTO detalle = service.obtenerDetallePorId(10L);

        assertThat(detalle.getPasajeroNombre()).isEqualTo("Juan Perez");
        assertThat(detalle.getConductorLicencia()).isEqualTo("ABC123");
        assertThat(detalle.getTarifaPrecioBase()).isEqualTo(new BigDecimal("3500.00"));
    }

    @Test
    void obtenerPorPasajero_retornaViajesDelPasajero() {
        when(repository.findByPasajeroId(1L)).thenReturn(List.of(entidad));

        assertThat(service.obtenerPorPasajero(1L)).hasSize(1);
    }
}
