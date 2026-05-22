package com.tienda.inventario.repositorio;

import com.tienda.inventario.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Patrón Repository - maneja el acceso a datos de productos
@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}