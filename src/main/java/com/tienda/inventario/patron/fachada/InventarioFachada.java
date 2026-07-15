package com.tienda.inventario.patron.fachada;

import com.tienda.inventario.controlador.dto.*;
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

    public ProductoResponseDTO agregarProducto(ProductoRequestDTO dto) {
        return InventarioMapper.toDTO(servicio.registrarProducto(InventarioMapper.toEntity(dto)));
    }

    public List<ProductoResponseDTO> obtenerProductos() {
        return InventarioMapper.toProductoDTOs(servicio.listarProductos());
    }

    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        return InventarioMapper.toDTO(servicio.obtenerProductoPorId(id));
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO dto) {
        return InventarioMapper.toDTO(servicio.actualizarProducto(id, InventarioMapper.toEntity(dto)));
    }

    // --- Almacenes ---

    public AlmacenResponseDTO agregarAlmacen(AlmacenRequestDTO dto) {
        return InventarioMapper.toDTO(servicio.registrarAlmacen(InventarioMapper.toEntity(dto)));
    }

    public List<AlmacenResponseDTO> obtenerAlmacenes() {
        return InventarioMapper.toAlmacenDTOs(servicio.listarAlmacenes());
    }

    // --- Stock ---

    public StockResponseDTO actualizarStock(StockRequestDTO dto) {
        return InventarioMapper.toDTO(servicio.actualizarStock(
                dto.getProductoId(),
                dto.getAlmacenId(),
                dto.getCantidad(),
                dto.getNotas()
        ));
    }

    public List<StockResponseDTO> stockPorProducto(Long productoId) {
        return InventarioMapper.toStockDTOs(servicio.verStockPorProducto(productoId));
    }

    public List<StockResponseDTO> stockPorAlmacen(Long almacenId) {
        return InventarioMapper.toStockDTOs(servicio.verStockPorAlmacen(almacenId));
    }

    // --- Reservas ---

    public String reservar(ReservaRequestDTO dto) {
        return servicio.reservarStock(dto.getProductoId(), dto.getAlmacenId(), dto.getCantidad(), dto.getReferencia());
    }

    public String cancelarReserva(String referencia) {
        return servicio.cancelarReserva(referencia);
    }

    // --- Transferencias ---

    public String transferir(TransferenciaRequestDTO dto) {
        return servicio.transferirStock(
                dto.getProductoId(),
                dto.getOrigenId(),
                dto.getDestinoId(),
                dto.getCantidad(),
                dto.getEstrategia()
        );
    }

    // --- Alertas y Reportes ---

    public List<StockResponseDTO> alertasStockBajo() {
        return InventarioMapper.toStockDTOs(servicio.verAlertasStockBajo());
    }

    public List<MovimientoResponseDTO> reporte(LocalDateTime desde, LocalDateTime hasta) {
        return InventarioMapper.toMovimientoDTOs(servicio.generarReporte(desde, hasta));
    }
}