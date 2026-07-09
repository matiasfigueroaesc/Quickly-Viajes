package com.duoc.ms_pasajeros.controller;

import com.duoc.ms_pasajeros.exception.PasajeroNotFoundException;
import com.duoc.ms_pasajeros.model.dto.PasajeroDTO;
import com.duoc.ms_pasajeros.model.entity.Pasajero;
import com.duoc.ms_pasajeros.service.PasajeroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PasajeroController.class)
class PasajeroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasajeroService service;

    private final Faker faker = new Faker();

    private Pasajero pasajero;
    private PasajeroDTO dto;

    @BeforeEach
    void setUp() {
        pasajero = new Pasajero();
        pasajero.setId(1L);
        pasajero.setNombre(faker.name().fullName());
        pasajero.setEmail(faker.internet().emailAddress());
        pasajero.setTelefono("912345678");

        dto = new PasajeroDTO();
        dto.setNombre(pasajero.getNombre());
        dto.setEmail(pasajero.getEmail());
        dto.setTelefono(pasajero.getTelefono());
    }

    @Test
    void listar_retorna200ConColeccion() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(pasajero));

        mockMvc.perform(get("/api/pasajeros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pasajeroList[0].email").value(pasajero.getEmail()));
    }

    @Test
    void obtenerPorId_existente_retorna200() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(pasajero);

        mockMvc.perform(get("/api/pasajeros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(pasajero.getNombre()));
    }

    @Test
    void obtenerPorId_inexistente_retorna404() throws Exception {
        when(service.obtenerPorId(99L)).thenThrow(new PasajeroNotFoundException(99L));

        mockMvc.perform(get("/api/pasajeros/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404));
    }

    @Test
    void crear_datosValidos_retorna201() throws Exception {
        when(service.guardar(any(PasajeroDTO.class))).thenReturn(pasajero);

        mockMvc.perform(post("/api/pasajeros")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(pasajero.getEmail()));
    }

    @Test
    void crear_nombreVacio_retorna400() throws Exception {
        dto.setNombre("");

        mockMvc.perform(post("/api/pasajeros")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombre").exists());
    }

    @Test
    void crear_emailInvalido_retorna400() throws Exception {
        dto.setEmail("no-es-un-email");

        mockMvc.perform(post("/api/pasajeros")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.email").exists());
    }

    @Test
    void crear_emailDuplicado_retorna409() throws Exception {
        // Cubre el bug fix de FASE 5: antes retornaba 500 en vez de 409
        when(service.guardar(any(PasajeroDTO.class)))
                .thenThrow(new IllegalArgumentException("Ya existe un pasajero registrado con el email: " + dto.getEmail()));

        mockMvc.perform(post("/api/pasajeros")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.estado").value(409));
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(service.actualizar(eq(1L), any(PasajeroDTO.class))).thenReturn(pasajero);

        mockMvc.perform(put("/api/pasajeros/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(pasajero.getEmail()));
    }

    @Test
    void eliminar_existente_retorna200() throws Exception {
        mockMvc.perform(delete("/api/pasajeros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("eliminado")));
    }
}
