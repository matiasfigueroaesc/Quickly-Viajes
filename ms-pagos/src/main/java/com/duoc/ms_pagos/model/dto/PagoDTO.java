package com.duoc.ms_pagos.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoDTO {

    @NotNull(message = "El ID del viaje no puede ser nulo")
    @Positive(message = "El ID del viaje debe ser un número positivo")
    private Long viajeId;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Digits(integer = 8, fraction = 2, message = "El monto no puede tener más de 8 enteros y 2 decimales")
    private BigDecimal monto;

    /**
     * Valores permitidos: TARJETA_CREDITO, TARJETA_DEBITO, EFECTIVO, TRANSFERENCIA
     * La validación del valor se realiza en la capa de servicio.
     */
    @NotBlank(message = "El método de pago no puede estar vacío")
    private String metodoPago;

    /**
     * Valores permitidos: PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO
     */
    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    @NotNull(message = "La fecha de pago no puede ser nula")
    private LocalDateTime fechaPago;
}
