CREATE TABLE notificaciones (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id    BIGINT NOT NULL,
    tipo        VARCHAR(30) NOT NULL,
    mensaje     VARCHAR(500) NOT NULL,
    canal       VARCHAR(20) NOT NULL,
    estado      VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_envio TIMESTAMP
);

INSERT INTO notificaciones (viaje_id, tipo, mensaje, canal, estado, fecha_envio)
VALUES (1, 'INICIO_VIAJE', 'Tu viaje ha comenzado. ¡Buen viaje!', 'EMAIL', 'ENVIADA', CURRENT_TIMESTAMP);

INSERT INTO notificaciones (viaje_id, tipo, mensaje, canal, estado, fecha_envio)
VALUES (1, 'FIN_VIAJE', 'Tu viaje ha finalizado. Gracias por usar nuestro servicio.', 'PUSH', 'ENVIADA', CURRENT_TIMESTAMP);
