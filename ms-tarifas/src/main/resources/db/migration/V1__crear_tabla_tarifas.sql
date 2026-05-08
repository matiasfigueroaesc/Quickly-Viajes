CREATE TABLE tarifas (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100)   NOT NULL UNIQUE,
    precio_base DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(255)   NOT NULL
);

INSERT INTO tarifas (nombre, precio_base, descripcion)
VALUES ('Tarifa Normal', 1500.00, 'Tarifa estándar para viajes regulares');

INSERT INTO tarifas (nombre, precio_base, descripcion)
VALUES ('Tarifa Premium', 3000.00, 'Tarifa para vehículos de alta gama');
