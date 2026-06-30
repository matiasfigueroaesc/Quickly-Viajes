package com.duoc.ms_auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String tipo; // "Bearer"
    private String username;
    private String rol;
    private long expiraEnMs;

}
