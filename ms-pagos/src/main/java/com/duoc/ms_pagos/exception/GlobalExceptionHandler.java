package com.duoc.ms_pagos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja errores de Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        log.warn("Error de validación en la ruta: {}", request.getRequestURI());

        Map<String, String> errores = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.BAD_REQUEST.value());
        respuesta.setError("Bad Request");
        respuesta.setMensaje("Los datos enviados no son válidos");
        respuesta.setRuta(request.getRequestURI());
        respuesta.setErrores(errores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // Maneja pago no encontrado → 404
    @ExceptionHandler(PagoNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarPagoNoEncontrado(PagoNotFoundException ex,
                                                                  HttpServletRequest request) {
        log.warn("Pago no encontrado. Ruta: {} - Mensaje: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.NOT_FOUND.value());
        respuesta.setError("Not Found");
        respuesta.setMensaje(ex.getMessage());
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // Maneja errores al comunicarse con ms-viajes (WebClient)
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> manejarErrorMicroservicio(WebClientResponseException ex,
                                                                    HttpServletRequest request) {
        log.warn("Error al comunicarse con microservicio externo. Ruta: {} - Status: {} - Detalle: {}",
                request.getRequestURI(), ex.getStatusCode(), ex.getMessage());

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(ex.getStatusCode().value());
        respuesta.setError("Error en microservicio externo");
        respuesta.setMensaje("No se pudo verificar el viaje en el servicio dependiente: " + ex.getMessage());
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(ex.getStatusCode()).body(respuesta);
    }

    // Maneja reglas de negocio violadas → 409 Conflict
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarConflicto(IllegalArgumentException ex,
                                                           HttpServletRequest request) {
        log.warn("Conflicto de negocio en la ruta: {} - {}", request.getRequestURI(), ex.getMessage());

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.CONFLICT.value());
        respuesta.setError("Conflict");
        respuesta.setMensaje(ex.getMessage());
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    // Maneja cualquier error inesperado → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(Exception ex,
                                                              HttpServletRequest request) {
        log.error("Error inesperado en la ruta: {}", request.getRequestURI(), ex);

        ErrorResponse respuesta = new ErrorResponse();
        respuesta.setFecha(LocalDateTime.now());
        respuesta.setEstado(HttpStatus.INTERNAL_SERVER_ERROR.value());
        respuesta.setError("Internal Server Error");
        respuesta.setMensaje("Ha ocurrido un error inesperado en el servidor");
        respuesta.setRuta(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
