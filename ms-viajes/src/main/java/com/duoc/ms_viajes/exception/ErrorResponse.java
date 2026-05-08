package com.duoc.ms_viajes.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ErrorResponse {

    private LocalDateTime fecha;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;
    private Map<String, String> errores; // solo para errores de validación
}
