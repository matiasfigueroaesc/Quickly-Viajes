package com.duoc.ms_conductores.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConductorDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un formato de correo válido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "El teléfono debe tener entre 9 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "El número de licencia no puede estar vacío")
    @Size(min = 5, max = 20, message = "La licencia debe tener entre 5 y 20 caracteres")
    private String licencia;

    @NotNull(message = "El campo activo no puede ser nulo")
    private Boolean activo;
}
