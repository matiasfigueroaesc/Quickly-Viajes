CREATE TABLE conductores (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    telefono   VARCHAR(20)  NOT NULL,
    licencia   VARCHAR(20)  NOT NULL UNIQUE,
    activo     BOOLEAN      NOT NULL DEFAULT TRUE
);

INSERT INTO conductores (nombre, email, telefono, licencia, activo)
VALUES ('Carlos Soto', 'carlos@gmail.com', '+56987654321', 'B-123456', TRUE);
