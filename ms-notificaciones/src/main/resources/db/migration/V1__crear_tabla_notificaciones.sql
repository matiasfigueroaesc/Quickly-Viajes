-- Tabla de notificaciones
CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    mensaje VARCHAR(500) NOT NULL,
    canal VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_envio TIMESTAMP NULL,
    INDEX idx_viaje (viaje_id),
    INDEX idx_tipo (tipo),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
