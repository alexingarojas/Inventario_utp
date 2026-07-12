package com.tienda.inventario.dto;

import com.tienda.inventario.modelo.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoResponseDTO {
    private Long id;
    private Long productoId;
    private String productoCodigo;
    private String productoNombre;
    private Long almacenOrigenId;
    private String almacenOrigenCodigo;
    private Long almacenDestinoId;
    private String almacenDestinoCodigo;
    private Movimiento.TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private String referencia;
    private String notas;
    private LocalDateTime fechaMovimiento;
}

