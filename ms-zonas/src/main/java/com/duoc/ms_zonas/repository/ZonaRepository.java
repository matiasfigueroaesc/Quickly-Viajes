package com.duoc.ms_zonas.repository;

import com.duoc.ms_zonas.model.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {

    boolean existsByNombre(String nombre);

    // Consulta para obtener solo las zonas activas
    List<Zona> findByActivaTrue();
}
