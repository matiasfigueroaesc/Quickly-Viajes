package com.duoc.ms_viajes.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al ID del pasajero en ms-pasajeros (no FK real, microservicios independientes)
    @Column(name = "pasajero_id", nullable = false)
    private Long pasajeroId;

    // Referencia al ID del conductor en ms-conductores
    @Column(name = "conductor_id", nullable = false)
    private Long conductorId;

    // Referencia al ID de la tarifa en ms-tarifas
    @Column(name = "tarifa_id", nullable = false)
    private Long tarifaId;

    @Column(nullable = false, length = 255)
    private String origen;

    @Column(nullable = false, length = 255)
    private String destino;

    /**
     * Estado del viaje: PENDIENTE, EN_CURSO, COMPLETADO, CANCELADO
     */
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(name = "monto_total", precision = 10, scale = 2)
    private BigDecimal montoTotal;
}
