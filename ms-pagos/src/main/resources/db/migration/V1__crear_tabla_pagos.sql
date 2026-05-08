CREATE TABLE pagos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id        BIGINT          NOT NULL,
    monto           DECIMAL(10, 2)  NOT NULL,
    metodo_pago     VARCHAR(30)     NOT NULL,
    estado          VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    fecha_pago      TIMESTAMP       NOT NULL
);

INSERT INTO pagos (viaje_id, monto, metodo_pago, estado, fecha_pago)
VALUES (1, 12500.00, 'TARJETA_CREDITO', 'COMPLETADO', CURRENT_TIMESTAMP);
