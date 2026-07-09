package com.duoc.ms_notificaciones.repository;

import com.duoc.ms_notificaciones.model.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Busca todas las notificaciones asociadas a un viaje
    List<Notificacion> findByViajeId(Long viajeId);

    // Busca por estado (PENDIENTE, ENVIADA, FALLIDA)
    List<Notificacion> findByEstado(String estado);

}
