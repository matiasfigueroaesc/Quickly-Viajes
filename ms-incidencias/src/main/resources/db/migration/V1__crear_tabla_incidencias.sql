CREATE TABLE incidencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    reportado_por BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'ABIERTA',
    fecha_reporte TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_resolucion TIMESTAMP
);

INSERT INTO incidencias (viaje_id, reportado_por, tipo, descripcion, estado, fecha_reporte)
VALUES (1, 1, 'ACCIDENTE', 'Choque menor al llegar al destino', 'ABIERTA', CURRENT_TIMESTAMP);

INSERT INTO incidencias (viaje_id, reportado_por, tipo, descripcion, estado, fecha_reporte)
VALUES (2, 2, 'OBJETO_PERDIDO', 'El pasajero olvidó su mochila en el vehículo', 'EN_REVISION', CURRENT_TIMESTAMP);
