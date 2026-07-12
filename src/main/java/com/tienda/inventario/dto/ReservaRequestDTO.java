package com.tienda.inventario.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ReservaRequestDTO {

    @NotNull(message = "productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "almacenId es obligatorio")
    private Long almacenId;

    @NotNull(message = "cantidad es obligatoria")
    @Positive(message = "cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "referencia es obligatoria")
    private String referencia;
}

