package com.tienda.inventario.controlador.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaRequestDTO {

    @NotNull(message = "productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "origenId es obligatorio")
    private Long origenId;

    @NotNull(message = "destinoId es obligatorio")
    private Long destinoId;

    @NotNull(message = "cantidad es obligatoria")
    @Positive(message = "cantidad debe ser mayor a 0")
    private Integer cantidad;

    private String estrategia;
}

