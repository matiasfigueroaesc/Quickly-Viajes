package com.duoc.ms_incidencias.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    // ID del usuario (pasajero o conductor) que reporta
    @Column(name = "reportado_por", nullable = false)
    private Long reportadoPor;

    // Tipo de incidencia: ACCIDENTE, OBJETO_PERDIDO, COMPORTAMIENTO_INAPROPIADO, OTRO
    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    // Estado: ABIERTA, EN_REVISION, RESUELTA, CERRADA
    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @PrePersist
    public void asignarFecha() {
        this.fechaReporte = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "ABIERTA";
        }
    }
}
