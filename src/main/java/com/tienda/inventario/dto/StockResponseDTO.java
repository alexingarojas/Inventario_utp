package com.tienda.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Long almacenId;
    private String almacenCodigo;
    private String almacenNombre;
    private Integer cantidad;
    private Integer cantidadReservada;
    private Integer cantidadDisponible;
    private LocalDateTime ultimaActualizacion;
}

