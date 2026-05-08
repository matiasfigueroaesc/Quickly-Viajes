package com.duoc.ms_vehiculos.repository;

import com.duoc.ms_vehiculos.model.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Spring Data genera la query automáticamente por convención de nombre
    boolean existsByPatente(String patente);

}
