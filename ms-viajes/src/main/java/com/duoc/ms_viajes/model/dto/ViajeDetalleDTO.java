package com.duoc.ms_viajes.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta que enriquece el viaje con información
 * obtenida de ms-pasajeros, ms-conductores y ms-tarifas vía WebClient.
 */
@Data
public class ViajeDetalleDTO {

    private Long id;
    private String origen;
    private String destino;
    private String estado;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal montoTotal;

    // Datos del pasajero obtenidos desde ms-pasajeros
    private Long pasajeroId;
    private String pasajeroNombre;
    private String pasajeroEmail;

    // Datos del conductor obtenidos desde ms-conductores
    private Long conductorId;
    private String conductorNombre;
    private String conductorLicencia;

    // Datos de la tarifa obtenidos desde ms-tarifas
    private Long tarifaId;
    private String tarifaNombre;
    private BigDecimal tarifaPrecioBase;
}
