CREATE TABLE zonas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO zonas (nombre, descripcion, activa) VALUES ('Zona Norte', 'Cubre el sector norte de la ciudad', TRUE);
INSERT INTO zonas (nombre, descripcion, activa) VALUES ('Zona Sur', 'Cubre el sector sur de la ciudad', TRUE);
INSERT INTO zonas (nombre, descripcion, activa) VALUES ('Zona Centro', 'Cubre el centro de la ciudad', TRUE);
