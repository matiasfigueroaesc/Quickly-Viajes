CREATE TABLE viajes (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    pasajero_id     BIGINT          NOT NULL,
    conductor_id    BIGINT          NOT NULL,
    tarifa_id       BIGINT          NOT NULL,
    origen          VARCHAR(255)    NOT NULL,
    destino         VARCHAR(255)    NOT NULL,
    estado          VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha_inicio    TIMESTAMP       NOT NULL,
    fecha_fin       TIMESTAMP,
    monto_total     DECIMAL(10, 2)
);

INSERT INTO viajes (pasajero_id, conductor_id, tarifa_id, origen, destino, estado, fecha_inicio, monto_total)
VALUES (1, 1, 1, 'Av. Providencia 1234', 'Aeropuerto SCL', 'COMPLETADO', CURRENT_TIMESTAMP, 12500.00);
