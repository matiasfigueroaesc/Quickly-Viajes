-- Tabla de viajes
CREATE TABLE viajes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pasajero_id BIGINT NOT NULL,
    conductor_id BIGINT NOT NULL,
    tarifa_id BIGINT NOT NULL,
    origen VARCHAR(255) NOT NULL,
    destino VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NULL,
    monto_total DECIMAL(10, 2),
    INDEX idx_pasajero (pasajero_id),
    INDEX idx_conductor (conductor_id),
    INDEX idx_tarifa (tarifa_id),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO viajes (pasajero_id, conductor_id, tarifa_id, origen, destino, estado, fecha_inicio, fecha_fin, monto_total)
VALUES (1, 1, 1, 'San Isidro', 'La Florida', 'COMPLETADO', NOW(), NOW(), 15000.00);
