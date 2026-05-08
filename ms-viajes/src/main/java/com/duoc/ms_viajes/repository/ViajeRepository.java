package com.duoc.ms_viajes.repository;

import com.duoc.ms_viajes.model.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {

    // Consulta todos los viajes de un pasajero específico
    List<Viaje> findByPasajeroId(Long pasajeroId);

    // Consulta todos los viajes de un conductor específico
    List<Viaje> findByConductorId(Long conductorId);

    // Consulta viajes según su estado (PENDIENTE, EN_CURSO, etc.)
    List<Viaje> findByEstado(String estado);

    // Verifica si un conductor tiene viajes en curso (para regla de negocio)
    boolean existsByConductorIdAndEstado(Long conductorId, String estado);
}
