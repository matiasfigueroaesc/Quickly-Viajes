package com.duoc.ms_pagos.exception;

public class PagoNotFoundException extends RuntimeException {

    public PagoNotFoundException(Long id) {
        super("Pago no encontrado con id: " + id);
    }
}
