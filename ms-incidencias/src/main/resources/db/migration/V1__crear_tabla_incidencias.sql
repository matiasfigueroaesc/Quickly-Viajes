-- Tabla de incidencias
CREATE TABLE incidencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    reportado_por BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ABIERTA',
    fecha_reporte TIMESTAMP NOT NULL,
    fecha_resolucion TIMESTAMP NULL,
    INDEX idx_viaje (viaje_id),
    INDEX idx_tipo (tipo),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
