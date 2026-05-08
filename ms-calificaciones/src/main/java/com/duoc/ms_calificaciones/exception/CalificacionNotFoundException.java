package com.duoc.ms_calificaciones.exception;

public class CalificacionNotFoundException extends RuntimeException {

    public CalificacionNotFoundException(Long id) {
        super("No se encontró una calificación con el id: " + id);
    }
}
