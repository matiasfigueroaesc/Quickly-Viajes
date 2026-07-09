-- Tabla de vehículos
CREATE TABLE vehiculos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conductor_id BIGINT NOT NULL,
    patente VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_conductor (conductor_id),
    INDEX idx_patente (patente),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO vehiculos (conductor_id, patente, marca, modelo, anio, tipo, activo)
VALUES (1, 'WXYZ123', 'Toyota', 'Corolla', 2020, 'SEDAN', TRUE);
