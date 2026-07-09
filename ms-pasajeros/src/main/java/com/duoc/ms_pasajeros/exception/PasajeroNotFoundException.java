package com.duoc.ms_pasajeros.exception;

public class PasajeroNotFoundException extends RuntimeException {

    public PasajeroNotFoundException(Long id) {
        super("Pasajero no encontrado con id: " + id);
    }

}
