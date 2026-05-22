package com.tienda.inventario.fachada;

import com.tienda.inventario.modelo.*;
import com.tienda.inventario.servicio.InventarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

// Patrón Facade - simplifica el acceso a las operaciones del inventario
@Component
@RequiredArgsConstructor
public class InventarioFachada {

    private final InventarioServicio servicio;

    // --- Productos ---

    public Producto agregarProducto(Producto producto) {
        return servicio.registrarProducto(producto);
    }

    public List<Producto> obtenerProductos() {
        return servicio.listarProductos();
    }

    // --- Almacenes ---

    public Almacen agregarAlmacen(Almacen almacen) {
        return servicio.registrarAlmacen(almacen);
    }

    public List<Almacen> obtenerAlmacenes() {
        return servicio.listarAlmacenes();
    }

    // --- Stock ---

    public Stock actualizarStock(Long productoId, Long almacenId, Integer cantidad, String notas) {
        return servicio.actualizarStock(productoId, almacenId, cantidad, notas);
    }

    public List<Stock> stockPorProducto(Long productoId) {
        return servicio.verStockPorProducto(productoId);
    }

    public List<Stock> stockPorAlmacen(Long almacenId) {
        return servicio.verStockPorAlmacen(almacenId);
    }

    // --- Reservas ---

    public String reservar(Long productoId, Long almacenId, Integer cantidad, String referencia) {
        return servicio.reservarStock(productoId, almacenId, cantidad, referencia);
    }

    public String cancelarReserva(String referencia) {
        return servicio.cancelarReserva(referencia);
    }

    // --- Transferencias ---

    public String transferir(Long productoId, Long origenId, Long destinoId,
                             Integer cantidad, String estrategia) {
        return servicio.transferirStock(productoId, origenId, destinoId, cantidad, estrategia);
    }

    // --- Alertas y Reportes ---

    public List<Stock> alertasStockBajo() {
        return servicio.verAlertasStockBajo();
    }

    public List<Movimiento> reporte(LocalDateTime desde, LocalDateTime hasta) {
        return servicio.generarReporte(desde, hasta);
    }
}