CREATE TABLE pasajeros (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           telefono VARCHAR(20) NOT NULL
);

INSERT INTO pasajeros (nombre, email, telefono) VALUES ('Juan Perez', 'juan@gmail.com', '+56912345678');