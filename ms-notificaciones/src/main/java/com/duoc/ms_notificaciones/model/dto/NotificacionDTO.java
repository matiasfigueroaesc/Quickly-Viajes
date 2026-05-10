package com.duoc.ms_notificaciones.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificacionDTO {

    @NotNull(message = "El ID del viaje no puede estar vacío")
    @Positive(message = "El ID del viaje debe ser un número positivo")
    private Long viajeId;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Pattern(
        regexp = "^(INICIO_VIAJE|FIN_VIAJE|CANCELACION|DEMORA|RECORDATORIO)$",
        message = "El tipo debe ser: INICIO_VIAJE, FIN_VIAJE, CANCELACION, DEMORA o RECORDATORIO"
    )
    private String tipo;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 5, max = 500, message = "El mensaje debe tener entre 5 y 500 caracteres")
    private String mensaje;

    @NotBlank(message = "El canal no puede estar vacío")
    @Pattern(
        regexp = "^(EMAIL|SMS|PUSH)$",
        message = "El canal debe ser: EMAIL, SMS o PUSH"
    )
    private String canal;
}
