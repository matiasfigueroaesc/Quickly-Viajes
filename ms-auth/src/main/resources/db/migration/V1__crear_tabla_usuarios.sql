CREATE TABLE usuarios (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           username VARCHAR(50) NOT NULL UNIQUE,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           rol VARCHAR(20) NOT NULL DEFAULT 'PASAJERO'
);

-- Usuario de ejemplo para pruebas locales.
-- Password en texto plano: "Admin123!" (hash BCrypt verificado para ese valor).
INSERT INTO usuarios (username, email, password, rol)
VALUES ('admin', 'admin@quicklyviajes.com', '$2b$10$X15tsvBN5AKQF43OSW0g6ueAaJ9owiVM.xhyqNxoYl8DBQ22r4tsS', 'ADMIN');
