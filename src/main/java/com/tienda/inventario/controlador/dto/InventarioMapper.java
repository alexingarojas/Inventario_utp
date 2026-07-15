package com.tienda.inventario.controlador.dto;

import com.tienda.inventario.modelo.Almacen;
import com.tienda.inventario.modelo.Movimiento;
import com.tienda.inventario.modelo.Producto;
import com.tienda.inventario.modelo.Stock;

import java.util.List;

public final class InventarioMapper {

    private InventarioMapper() {
    }

    public static Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stockMinimo(dto.getStockMinimo())
                .build();
    }

    public static ProductoResponseDTO toDTO(Producto entity) {
        return ProductoResponseDTO.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .precio(entity.getPrecio())
                .stockMinimo(entity.getStockMinimo())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

    public static List<ProductoResponseDTO> toProductoDTOs(List<Producto> entities) {
        return entities.stream().map(InventarioMapper::toDTO).toList();
    }

    public static Almacen toEntity(AlmacenRequestDTO dto) {
        return Almacen.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .direccion(dto.getDireccion())
                .ciudad(dto.getCiudad())
                .build();
    }

    public static AlmacenResponseDTO toDTO(Almacen entity) {
        return AlmacenResponseDTO.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

    public static List<AlmacenResponseDTO> toAlmacenDTOs(List<Almacen> entities) {
        return entities.stream().map(InventarioMapper::toDTO).toList();
    }

    public static StockResponseDTO toDTO(Stock entity) {
        return StockResponseDTO.builder()
                .id(entity.getId())
                .productoId(entity.getProducto().getId())
                .productoCodigo(entity.getProducto().getCodigo())
                .productoNombre(entity.getProducto().getNombre())
                .productoStockMinimo(entity.getProducto().getStockMinimo())
                .almacenId(entity.getAlmacen().getId())
                .almacenCodigo(entity.getAlmacen().getCodigo())
                .almacenNombre(entity.getAlmacen().getNombre())
                .cantidad(entity.getCantidad())
                .cantidadReservada(entity.getCantidadReservada())
                .cantidadDisponible(entity.getCantidadDisponible())
                .ultimaActualizacion(entity.getUltimaActualizacion())
                .build();
    }

    public static List<StockResponseDTO> toStockDTOs(List<Stock> entities) {
        return entities.stream().map(InventarioMapper::toDTO).toList();
    }

    public static MovimientoResponseDTO toDTO(Movimiento entity) {
        return MovimientoResponseDTO.builder()
                .id(entity.getId())
                .productoId(entity.getProducto().getId())
                .productoCodigo(entity.getProducto().getCodigo())
                .productoNombre(entity.getProducto().getNombre())
                .almacenOrigenId(entity.getAlmacenOrigen() != null ? entity.getAlmacenOrigen().getId() : null)
                .almacenOrigenCodigo(entity.getAlmacenOrigen() != null ? entity.getAlmacenOrigen().getCodigo() : null)
                .almacenDestinoId(entity.getAlmacenDestino() != null ? entity.getAlmacenDestino().getId() : null)
                .almacenDestinoCodigo(entity.getAlmacenDestino() != null ? entity.getAlmacenDestino().getCodigo() : null)
                .tipoMovimiento(entity.getTipoMovimiento())
                .cantidad(entity.getCantidad())
                .referencia(entity.getReferencia())
                .notas(entity.getNotas())
                .fechaMovimiento(entity.getFechaMovimiento())
                .build();
    }

    public static List<MovimientoResponseDTO> toMovimientoDTOs(List<Movimiento> entities) {
        return entities.stream().map(InventarioMapper::toDTO).toList();
    }
}

