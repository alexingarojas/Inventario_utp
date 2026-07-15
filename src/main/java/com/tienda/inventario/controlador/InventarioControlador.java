package com.tienda.inventario.controlador;

import com.tienda.inventario.controlador.dto.*;
import com.tienda.inventario.patron.fachada.InventarioFachada;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        return ResponseEntity.ok(fachada.agregarProducto(producto));
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(fachada.obtenerProductos());
    }

    @GetMapping("/productos/{id}/stock")
    public ResponseEntity<List<StockResponseDTO>> verStockProducto(@PathVariable Long id) {
        return ResponseEntity.ok(fachada.stockPorProducto(id));
    }

    // ALMACENES

    @PostMapping("/almacenes")
    public ResponseEntity<AlmacenResponseDTO> crearAlmacen(@Valid @RequestBody AlmacenRequestDTO almacen) {
        return ResponseEntity.ok(fachada.agregarAlmacen(almacen));
    }

    @GetMapping("/almacenes")
    public ResponseEntity<List<AlmacenResponseDTO>> listarAlmacenes() {
        return ResponseEntity.ok(fachada.obtenerAlmacenes());
    }

    @GetMapping("/almacenes/{id}/stock")
    public ResponseEntity<List<StockResponseDTO>> verStockAlmacen(@PathVariable Long id) {
        return ResponseEntity.ok(fachada.stockPorAlmacen(id));
    }

    // STOCK

    @PostMapping("/stock")
    public ResponseEntity<StockResponseDTO> actualizarStock(@Valid @RequestBody StockRequestDTO dto) {

        return ResponseEntity.ok(fachada.actualizarStock(dto));
    }

    // RESERVAS

    @PostMapping("/reservas")
    public ResponseEntity<String> reservar(@Valid @RequestBody ReservaRequestDTO dto) {

        return ResponseEntity.ok(fachada.reservar(dto));
    }

    @DeleteMapping("/reservas/{referencia}")
    public ResponseEntity<String> cancelarReserva(@PathVariable String referencia) {
        return ResponseEntity.ok(fachada.cancelarReserva(referencia));
    }

    // TRANSFERENCIAS

    @PostMapping("/transferencias")
    public ResponseEntity<String> transferir(@Valid @RequestBody TransferenciaRequestDTO dto) {

        return ResponseEntity.ok(fachada.transferir(dto));
    }

    // ALERTAS

    @GetMapping("/alertas")
    public ResponseEntity<List<StockResponseDTO>> verAlertas() {
        return ResponseEntity.ok(fachada.alertasStockBajo());
    }

    // REPORTES

    @GetMapping("/reporte")
    public ResponseEntity<List<MovimientoResponseDTO>> verReporte(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime desde,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime hasta) {

        return ResponseEntity.ok(fachada.reporte(desde, hasta));
    }
}