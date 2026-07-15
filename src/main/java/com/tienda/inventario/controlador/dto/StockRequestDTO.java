package com.tienda.inventario.controlador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {

    @NotNull(message = "productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "almacenId es obligatorio")
    private Long almacenId;

    @NotNull(message = "cantidad es obligatoria")
    @PositiveOrZero(message = "cantidad debe ser mayor o igual a 0")
    private Integer cantidad;

    private String notas;
}

