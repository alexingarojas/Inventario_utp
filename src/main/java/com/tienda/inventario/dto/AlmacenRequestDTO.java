package com.tienda.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlmacenRequestDTO {

    @NotBlank(message = "codigo es obligatorio")
    private String codigo;

    @NotBlank(message = "nombre es obligatorio")
    private String nombre;

    private String direccion;
    private String ciudad;
}

