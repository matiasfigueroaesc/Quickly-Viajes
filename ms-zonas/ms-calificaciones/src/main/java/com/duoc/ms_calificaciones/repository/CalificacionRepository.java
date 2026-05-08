package com.duoc.ms_calificaciones.repository;

import com.duoc.ms_calificaciones.model.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    List<Calificacion> findByConductorId(Long conductorId);

    List<Calificacion> findByPasajeroId(Long pasajeroId);

    // Regla de negocio: un pasajero solo puede calificar un viaje una vez
    boolean existsByViajeIdAndPasajeroId(Long viajeId, Long pasajeroId);

    // Obtener promedio de puntaje de un conductor
    @Query("SELECT AVG(c.puntaje) FROM Calificacion c WHERE c.conductorId = :conductorId")
    Double promedioCalificacionConductor(@Param("conductorId") Long conductorId);
}
