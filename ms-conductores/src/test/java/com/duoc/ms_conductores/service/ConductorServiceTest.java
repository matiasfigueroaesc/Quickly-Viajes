package com.duoc.ms_conductores.service;

import com.duoc.ms_conductores.exception.ConductorNotFoundException;
import com.duoc.ms_conductores.model.dto.ConductorDTO;
import com.duoc.ms_conductores.model.entity.Conductor;
import com.duoc.ms_conductores.repository.ConductorRepository;
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
class ConductorServiceTest {

    @Mock
    private ConductorRepository repository;

    @InjectMocks
    private ConductorService service;

    private final Faker faker = new Faker();

    private ConductorDTO dto;
    private Conductor entidad;

    @BeforeEach
    void setUp() {
        dto = new ConductorDTO();
        dto.setNombre(faker.name().fullName());
        dto.setEmail(faker.internet().emailAddress());
        dto.setTelefono("912345678");
        dto.setLicencia(faker.idNumber().valid());
        dto.setActivo(true);

        entidad = new Conductor();
        entidad.setId(1L);
        entidad.setNombre(dto.getNombre());
        entidad.setEmail(dto.getEmail());
        entidad.setTelefono(dto.getTelefono());
        entidad.setLicencia(dto.getLicencia());
        entidad.setActivo(true);
    }

    @Test
    void obtenerTodos_retornaListaCompleta() {
        when(repository.findAll()).thenReturn(List.of(entidad));

        assertThat(service.obtenerTodos()).hasSize(1);
    }

    @Test
    void obtenerPorId_existente_retornaConductor() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));

        assertThat(service.obtenerPorId(1L).getLicencia()).isEqualTo(dto.getLicencia());
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ConductorNotFoundException.class);
    }

    @Test
    void guardar_datosValidos_guardaCorrectamente() {
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repository.existsByLicencia(dto.getLicencia())).thenReturn(false);
        when(repository.save(any(Conductor.class))).thenReturn(entidad);

        Conductor resultado = service.guardar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
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
    void guardar_licenciaDuplicada_lanzaIllegalArgumentException() {
        when(repository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(repository.existsByLicencia(dto.getLicencia())).thenReturn(true);

        assertThatThrownBy(() -> service.guardar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(dto.getLicencia());

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(entidad));
        when(repository.save(any(Conductor.class))).thenAnswer(inv -> inv.getArgument(0));

        ConductorDTO nuevo = new ConductorDTO();
        nuevo.setNombre("Nombre Actualizado");
        nuevo.setEmail(dto.getEmail());
        nuevo.setTelefono(dto.getTelefono());
        nuevo.setLicencia(dto.getLicencia());
        nuevo.setActivo(false);

        Conductor resultado = service.actualizar(1L, nuevo);

        assertThat(resultado.getNombre()).isEqualTo("Nombre Actualizado");
        assertThat(resultado.getActivo()).isFalse();
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(5L, dto))
                .isInstanceOf(ConductorNotFoundException.class);
    }

    @Test
    void eliminar_existente_eliminaCorrectamente() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.existsById(77L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(77L))
                .isInstanceOf(ConductorNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
