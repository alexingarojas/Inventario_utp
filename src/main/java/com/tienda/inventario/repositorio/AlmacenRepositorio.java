package com.tienda.inventario.repositorio;

import com.tienda.inventario.modelo.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Patrón Repository - maneja el acceso a datos de almacenes
@Repository
public interface AlmacenRepositorio extends JpaRepository<Almacen, Long> {

    Optional<Almacen> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}