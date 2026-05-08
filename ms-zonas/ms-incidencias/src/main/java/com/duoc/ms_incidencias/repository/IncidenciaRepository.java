package com.duoc.ms_incidencias.repository;

import com.duoc.ms_incidencias.model.entity.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByViajeId(Long viajeId);

    List<Incidencia> findByEstado(String estado);

    List<Incidencia> findByReportadoPor(Long reportadoPor);
}
