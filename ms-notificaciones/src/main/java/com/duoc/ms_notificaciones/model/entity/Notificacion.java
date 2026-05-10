package com.duoc.ms_notificaciones.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "viaje_id", nullable = false)
    private Long viajeId;

    /**
     * Tipo de evento: INICIO_VIAJE, FIN_VIAJE, CANCELACION, DEMORA, etc.
     */
    @Column(nullable = false, length = 30)
    private String tipo;

    @Column(nullable = false, length = 500)
    private String mensaje;

    /**
     * Canal de envío: EMAIL, SMS, PUSH
     */
    @Column(nullable = false, length = 20)
    private String canal;

    /**
     * Estado: PENDIENTE, ENVIADA, FALLIDA
     */
    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}
