package com.duoc.ms_incidencias.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class IncidenciaDTO {

    @NotNull(message = "El id del viaje no puede ser nulo")
    @Positive(message = "El id del viaje debe ser un número positivo")
    private Long viajeId;

    @NotNull(message = "El id del reportante no puede ser nulo")
    @Positive(message = "El id del reportante debe ser un número positivo")
    private Long reportadoPor;

    @NotBlank(message = "El tipo de incidencia no puede estar vacío")
    @Pattern(
        regexp = "ACCIDENTE|OBJETO_PERDIDO|COMPORTAMIENTO_INAPROPIADO|OTRO",
        message = "El tipo debe ser: ACCIDENTE, OBJETO_PERDIDO, COMPORTAMIENTO_INAPROPIADO u OTRO"
    )
    private String tipo;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 1000, message = "La descripción debe tener entre 10 y 1000 caracteres")
    private String descripcion;
}
