package com.duoc.ms_conductores.exception;

public class ConductorNotFoundException extends RuntimeException {

    public ConductorNotFoundException(Long id) {
        super("Conductor no encontrado con id: " + id);
    }

}
