package com.duoc.ms_pagos.repository;

import com.duoc.ms_pagos.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Busca todos los pagos asociados a un viaje
    List<Pago> findByViajeId(Long viajeId);

    // Busca todos los pagos por estado (PENDIENTE, COMPLETADO, etc.)
    List<Pago> findByEstado(String estado);

    // Busca pagos por método de pago
    List<Pago> findByMetodoPago(String metodoPago);

    // Verifica si ya existe un pago COMPLETADO para un viaje (regla de negocio: no pagar dos veces)
    boolean existsByViajeIdAndEstado(Long viajeId, String estado);

    // Obtiene el pago activo (no rechazado) de un viaje
    Optional<Pago> findByViajeIdAndEstadoNot(Long viajeId, String estado);
}
