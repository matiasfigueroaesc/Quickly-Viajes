package com.duoc.ms_auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroDTO {

    @NotBlank(message = "El username no puede estar vacío")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un formato de correo válido")
    private String email;

    @NotBlank(message = "El password no puede estar vacío")
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "El password debe contener al menos una letra y un número"
    )
    private String password;

    // Rol opcional al registrarse; si no se envía, el service asigna PASAJERO por defecto
    private String rol;

}
