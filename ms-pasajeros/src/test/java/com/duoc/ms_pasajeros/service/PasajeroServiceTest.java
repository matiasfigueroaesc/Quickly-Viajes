package com.duoc.ms_pasajeros.service;

import com.duoc.ms_pasajeros.exception.PasajeroNotFoundException;
import com.duoc.ms_pasajeros.model.dto.PasajeroDTO;
import com.duoc.ms_pasajeros.model.entity.Pasajero;
import com.duoc.ms_pasajeros.repository.PasajeroRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasajeroServiceTest {

    @Mock
    private PasajeroRepository repository;

    @InjectMocks
    private PasajeroService service;

    private final Faker faker = new Faker();

    private PasajeroDTO dto;
    private Pasajero entidad;

    @BeforeEach
    void setUp() {
        dto = new PasajeroDTO();
        dto.setNombre(faker.name().fullName());
        dto.setEmail(faker.internet().emailAddress());
        dto.setTelefono("912345678");

        entidad = new Pasajero();
        entidad.setId(1L);
        entidad.setNombre(dto.getNombre());
        entidad.setEmail(dto.getEmail());
        entidad.setTelefono(dto.getTelefono());
    }

    @Test
    void obtenerTodos_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        List<Pasajero> resultado = service.obtenerTodos();

        assertThat(resultado).hasSize(1).contains(entidad);
        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_existente_retornaPasajero() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        Pasajero resultado = service.obtenerPorId(1L);

        assertThat(resultado.getEmail()).isEqualTo(entidad.getEmail());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(PasajeroNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void guardar_emailNuevo_guardaCorrectamente() {
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repository.save(any(Pasajero.class))).thenReturn(entidad);

        Pasajero resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(any(Pasajero.class));
    }

    @Test
    void guardar_emailDuplicado_lanzaIllegalArgumentException() {
        when(repository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(dto.getEmail());

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        PasajeroDTO nuevo = new PasajeroDTO();
        nuevo.setNombre(faker.name().fullName());
        nuevo.setEmail(faker.internet().emailAddress());
        nuevo.setTelefono("998877665");

        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Pasajero.class))).thenAnswer(inv -> inv.getArgument(0));

        Pasajero resultado = service.actualizar(1L, nuevo);

        assertThat(resultado.getNombre()).isEqualTo(nuevo.getNombre());
        assertThat(resultado.getEmail()).isEqualTo(nuevo.getEmail());
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, dto))
                .isInstanceOf(PasajeroNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(50L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(50L))
                .isInstanceOf(PasajeroNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
