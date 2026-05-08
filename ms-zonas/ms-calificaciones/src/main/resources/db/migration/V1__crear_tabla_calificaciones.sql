CREATE TABLE calificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    viaje_id BIGINT NOT NULL,
    pasajero_id BIGINT NOT NULL,
    conductor_id BIGINT NOT NULL,
    puntaje INT NOT NULL,
    comentario VARCHAR(500),
    fecha_calificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO calificaciones (viaje_id, pasajero_id, conductor_id, puntaje, comentario, fecha_calificacion)
VALUES (1, 1, 1, 5, 'Excelente conductor, muy puntual', CURRENT_TIMESTAMP);

INSERT INTO calificaciones (viaje_id, pasajero_id, conductor_id, puntaje, comentario, fecha_calificacion)
VALUES (2, 2, 1, 4, 'Buen servicio en general', CURRENT_TIMESTAMP);
