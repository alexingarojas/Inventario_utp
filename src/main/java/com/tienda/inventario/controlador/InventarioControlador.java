package com.tienda.inventario.controlador;

import com.tienda.inventario.fachada.InventarioFachada;
import com.tienda.inventario.modelo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InventarioControlador {

    private final InventarioFachada fachada;

    // PRODUCTOS

    @PostMapping("/productos")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(fachada.agregarProducto(producto));
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(fachada.obtenerProductos());
    }

    @GetMapping("/productos/{id}/stock")
    public ResponseEntity<List<Stock>> verStockProducto(@PathVariable Long id) {
        return ResponseEntity.ok(fachada.stockPorProducto(id));
    }

    // ALMACENES

    @PostMapping("/almacenes")
    public ResponseEntity<Almacen> crearAlmacen(@RequestBody Almacen almacen) {
        return ResponseEntity.ok(fachada.agregarAlmacen(almacen));
    }

    @GetMapping("/almacenes")
    public ResponseEntity<List<Almacen>> listarAlmacenes() {
        return ResponseEntity.ok(fachada.obtenerAlmacenes());
    }

    @GetMapping("/almacenes/{id}/stock")
    public ResponseEntity<List<Stock>> verStockAlmacen(@PathVariable Long id) {
        return ResponseEntity.ok(fachada.stockPorAlmacen(id));
    }

    // STOCK

    @PostMapping("/stock")
    public ResponseEntity<Stock> actualizarStock(
            @RequestParam Long productoId,
            @RequestParam Long almacenId,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String notas) {

        return ResponseEntity.ok(
                fachada.actualizarStock(productoId, almacenId, cantidad, notas)
        );
    }

    // RESERVAS

    @PostMapping("/reservas")
    public ResponseEntity<String> reservar(
            @RequestParam Long productoId,
            @RequestParam Long almacenId,
            @RequestParam Integer cantidad,
            @RequestParam String referencia) {

        return ResponseEntity.ok(
                fachada.reservar(productoId, almacenId, cantidad, referencia)
        );
    }

    @DeleteMapping("/reservas/{referencia}")
    public ResponseEntity<String> cancelarReserva(@PathVariable String referencia) {
        return ResponseEntity.ok(fachada.cancelarReserva(referencia));
    }

    // TRANSFERENCIAS

    @PostMapping("/transferencias")
    public ResponseEntity<String> transferir(
            @RequestParam Long productoId,
            @RequestParam Long origenId,
            @RequestParam Long destinoId,
            @RequestParam Integer cantidad,
            @RequestParam(required = false) String estrategia) {

        return ResponseEntity.ok(
                fachada.transferir(
                        productoId,
                        origenId,
                        destinoId,
                        cantidad,
                        estrategia
                )
        );
    }

    // ALERTAS

    @GetMapping("/alertas")
    public ResponseEntity<List<Stock>> verAlertas() {
        return ResponseEntity.ok(fachada.alertasStockBajo());
    }

    // REPORTES

    @GetMapping("/reporte")
    public ResponseEntity<List<Movimiento>> verReporte(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime hasta) {

        return ResponseEntity.ok(fachada.reporte(desde, hasta));
    }
}