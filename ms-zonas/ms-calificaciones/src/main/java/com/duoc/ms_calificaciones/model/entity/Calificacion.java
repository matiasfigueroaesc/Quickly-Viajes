package com.duoc.ms_calificaciones.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "calificaciones")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    @Column(name = "pasajero_id", nullable = false)
    private Long pasajeroId;

    @Column(name = "conductor_id", nullable = false)
    private Long conductorId;

    // Puntaje entre 1 y 5
    @Column(nullable = false)
    private Integer puntaje;

    @Column(length = 500)
    private String comentario;

    @Column(name = "fecha_calificacion", nullable = false)
    private LocalDateTime fechaCalificacion;

    @PrePersist
    public void asignarFecha() {
        this.fechaCalificacion = LocalDateTime.now();
    }
}
