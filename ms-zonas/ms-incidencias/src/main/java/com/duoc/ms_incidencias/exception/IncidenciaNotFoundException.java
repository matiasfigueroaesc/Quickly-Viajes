package com.duoc.ms_incidencias.exception;

public class IncidenciaNotFoundException extends RuntimeException {

    public IncidenciaNotFoundException(Long id) {
        super("No se encontró una incidencia con el id: " + id);
    }
}
