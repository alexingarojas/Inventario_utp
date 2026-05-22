package com.tienda.inventario.repositorio;

import com.tienda.inventario.modelo.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

// Patrón Repository - maneja el acceso a datos de stock
@Repository
public interface StockRepositorio extends JpaRepository<Stock, Long> {

    // Bloqueo pesimista para control de concurrencia
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Stock s WHERE s.producto.id = :productoId AND s.almacen.id = :almacenId")
    Optional<Stock> buscarConBloqueo(
            @Param("productoId") Long productoId,
            @Param("almacenId") Long almacenId);

    Optional<Stock> findByProductoIdAndAlmacenId(Long productoId, Long almacenId);

    List<Stock> findByProductoId(Long productoId);

    List<Stock> findByAlmacenId(Long almacenId);

    // Busca productos con stock bajo el mínimo
    @Query("SELECT s FROM Stock s WHERE (s.cantidad - s.cantidadReservada) <= s.producto.stockMinimo")
    List<Stock> buscarStockBajo();
}