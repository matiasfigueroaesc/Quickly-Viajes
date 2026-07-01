-- Tabla de pagos
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago TIMESTAMP NOT NULL,
    INDEX idx_viaje (viaje_id),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO pagos (viaje_id, monto, metodo_pago, estado, fecha_pago)
VALUES (1, 15000.00, 'TARJETA_CREDITO', 'COMPLETADO', NOW());
