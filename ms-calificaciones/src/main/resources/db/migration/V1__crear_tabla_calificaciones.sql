-- Tabla de calificaciones
CREATE TABLE calificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    pasajero_id BIGINT NOT NULL,
    conductor_id BIGINT NOT NULL,
    puntaje INT NOT NULL CHECK (puntaje >= 1 AND puntaje <= 5),
    comentario VARCHAR(500),
    fecha_calificacion TIMESTAMP NOT NULL,
    INDEX idx_viaje (viaje_id),
    INDEX idx_pasajero (pasajero_id),
    INDEX idx_conductor (conductor_id),
    INDEX idx_puntaje (puntaje)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO calificaciones (viaje_id, pasajero_id, conductor_id, puntaje, comentario, fecha_calificacion)
VALUES (1, 1, 1, 5, 'Excelente servicio', NOW());
