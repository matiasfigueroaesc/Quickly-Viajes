package com.duoc.ms_tarifas.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TarifaDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio base no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio base debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio base debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precioBase;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String descripcion;
}
