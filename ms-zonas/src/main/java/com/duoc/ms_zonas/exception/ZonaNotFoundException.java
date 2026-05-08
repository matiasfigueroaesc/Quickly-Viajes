package com.duoc.ms_zonas.exception;

public class ZonaNotFoundException extends RuntimeException {

    public ZonaNotFoundException(Long id) {
        super("No se encontró una zona con el id: " + id);
    }
}
