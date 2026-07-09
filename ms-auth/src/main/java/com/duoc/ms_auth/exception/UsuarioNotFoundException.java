package com.duoc.ms_auth.exception;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }

    public UsuarioNotFoundException(String username) {
        super("Usuario no encontrado: " + username);
    }

}
