package com.duoc.ms_calificaciones.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CalificacionDTO {

    @NotNull(message = "El id del viaje no puede ser nulo")
    @Positive(message = "El id del viaje debe ser un número positivo")
    private Long viajeId;

    @NotNull(message = "El id del pasajero no puede ser nulo")
    @Positive(message = "El id del pasajero debe ser un número positivo")
    private Long pasajeroId;

    @NotNull(message = "El id del conductor no puede ser nulo")
    @Positive(message = "El id del conductor debe ser un número positivo")
    private Long conductorId;

    @NotNull(message = "El puntaje no puede ser nulo")
    @Min(value = 1, message = "El puntaje mínimo es 1")
    @Max(value = 5, message = "El puntaje máximo es 5")
    private Integer puntaje;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comentario;
}
