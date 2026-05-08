package com.duoc.ms_viajes.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ViajeDTO {

    @NotNull(message = "El ID del pasajero no puede ser nulo")
    @Positive(message = "El ID del pasajero debe ser un número positivo")
    private Long pasajeroId;

    @NotNull(message = "El ID del conductor no puede ser nulo")
    @Positive(message = "El ID del conductor debe ser un número positivo")
    private Long conductorId;

    @NotNull(message = "El ID de la tarifa no puede ser nulo")
    @Positive(message = "El ID de la tarifa debe ser un número positivo")
    private Long tarifaId;

    @NotBlank(message = "El origen no puede estar vacío")
    @Size(min = 5, max = 255, message = "El origen debe tener entre 5 y 255 caracteres")
    private String origen;

    @NotBlank(message = "El destino no puede estar vacío")
    @Size(min = 5, max = 255, message = "El destino debe tener entre 5 y 255 caracteres")
    private String destino;

    /**
     * Estado permitido: PENDIENTE, EN_CURSO, COMPLETADO, CANCELADO
     * La validación del valor se realiza en la capa de servicio.
     */
    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDateTime fechaInicio;

    // fecha_fin y monto_total son opcionales al crear; se asignan al completar el viaje
    private LocalDateTime fechaFin;

    @DecimalMin(value = "0.0", inclusive = false, message = "El monto total debe ser mayor a cero")
    private BigDecimal montoTotal;
}
