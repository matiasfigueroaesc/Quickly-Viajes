package com.duoc.ms_pagos.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al ID del viaje en ms-viajes (no FK real, microservicios independientes)
    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    /**
     * Método de pago: TARJETA_CREDITO, TARJETA_DEBITO, EFECTIVO, TRANSFERENCIA
     */
    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago;

    /**
     * Estado del pago: PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO
     */
    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;
}
