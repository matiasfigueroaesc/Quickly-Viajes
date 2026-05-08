package com.duoc.ms_conductores.repository;

import com.duoc.ms_conductores.model.entity.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConductorRepository extends JpaRepository<Conductor, Long> {

    // Spring Data genera la query automáticamente por convención de nombre
    boolean existsByEmail(String email);

    boolean existsByLicencia(String licencia);

}
