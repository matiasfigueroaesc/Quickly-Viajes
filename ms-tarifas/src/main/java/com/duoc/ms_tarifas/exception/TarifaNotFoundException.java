package com.duoc.ms_tarifas.exception;

public class TarifaNotFoundException extends RuntimeException {

    public TarifaNotFoundException(Long id) {
        super("Tarifa no encontrada con id: " + id);
    }

}
