package com.duoc.ms_auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO de salida: nunca incluye el password ni su hash.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String rol;

}
