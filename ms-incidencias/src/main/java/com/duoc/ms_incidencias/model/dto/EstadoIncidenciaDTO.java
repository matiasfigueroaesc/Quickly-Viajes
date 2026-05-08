package com.duoc.ms_incidencias.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class EstadoIncidenciaDTO {

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(
        regexp = "ABIERTA|EN_REVISION|RESUELTA|CERRADA",
        message = "El estado debe ser: ABIERTA, EN_REVISION, RESUELTA o CERRADA"
    )
    private String estado;
}
