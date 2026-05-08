package com.duoc.ms_viajes.exception;

public class ViajeNotFoundException extends RuntimeException {

    public ViajeNotFoundException(Long id) {
        super("Viaje no encontrado con id: " + id);
    }
}
