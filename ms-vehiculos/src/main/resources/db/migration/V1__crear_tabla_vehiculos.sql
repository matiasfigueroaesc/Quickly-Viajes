CREATE TABLE vehiculos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patente VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    tipo VARCHAR(30) NOT NULL
);

INSERT INTO vehiculos (patente, marca, modelo, anio, tipo) VALUES ('ABCD12', 'Toyota', 'Corolla', 2020, 'Sedan');
INSERT INTO vehiculos (patente, marca, modelo, anio, tipo) VALUES ('EFGH34', 'Chevrolet', 'Spark', 2019, 'Hatchback');
