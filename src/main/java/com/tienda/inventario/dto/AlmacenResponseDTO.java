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
public class AlmacenResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private LocalDateTime fechaRegistro;
}

