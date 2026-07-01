-- Tabla de pasajeros
-- Motor: InnoDB, Charset: UTF8MB4 (soporte para emojis, caracteres especiales)
CREATE TABLE pasajeros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO pasajeros (nombre, email, telefono) 
VALUES ('Juan Perez', 'juan@gmail.com', '+56912345678');
