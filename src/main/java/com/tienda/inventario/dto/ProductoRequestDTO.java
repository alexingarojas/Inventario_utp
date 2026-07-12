package com.tienda.inventario.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "codigo es obligatorio")
    private String codigo;

    @NotBlank(message = "nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "precio debe ser mayor o igual a 0")
    private BigDecimal precio;

    @NotNull(message = "stockMinimo es obligatorio")
    @PositiveOrZero(message = "stockMinimo debe ser mayor o igual a 0")
    private Integer stockMinimo;
}

