package com.duoc.ms_vehiculos.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VehiculoDTO {

    @NotBlank(message = "La patente no puede estar vacía")
    @Size(min = 6, max = 10, message = "La patente debe tener entre 6 y 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "La patente debe contener solo letras mayúsculas y números")
    private String patente;

    @NotBlank(message = "La marca no puede estar vacía")
    @Size(min = 2, max = 50, message = "La marca debe tener entre 2 y 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo no puede estar vacío")
    @Size(min = 1, max = 50, message = "El modelo debe tener entre 1 y 50 caracteres")
    private String modelo;

    @NotNull(message = "El año no puede estar vacío")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2100, message = "El año no puede ser mayor a 2100")
    private Integer anio;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Size(min = 2, max = 30, message = "El tipo debe tener entre 2 y 30 caracteres")
    private String tipo;
}
