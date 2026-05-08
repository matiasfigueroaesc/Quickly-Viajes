package com.duoc.ms_pasajeros.repository;

import com.duoc.ms_pasajeros.model.entity.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {

    // Spring Data genera la query automáticamente por convención de nombre
    boolean existsByEmail(String email);

}
