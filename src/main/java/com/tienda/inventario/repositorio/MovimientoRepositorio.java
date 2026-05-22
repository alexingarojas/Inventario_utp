package com.tienda.inventario.repositorio;

import com.tienda.inventario.modelo.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

// Patrón Repository - maneja el acceso a datos de movimientos
@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByProductoIdOrderByFechaMovimientoDesc(Long productoId);

    // Busca movimientos en un rango de fechas para el reporte
    @Query("SELECT m FROM Movimiento m WHERE m.fechaMovimiento BETWEEN :desde AND :hasta ORDER BY m.fechaMovimiento DESC")
    List<Movimiento> buscarPorFechas(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);
}