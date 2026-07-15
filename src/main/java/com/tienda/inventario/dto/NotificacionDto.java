package com.tienda.inventario.dto;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDto {
    
    private String productoNombre;
    private String almacenNombre;
    private Integer disponible;
    private Integer minimo;

    @Builder.Default
    private LocalDateTime fechaAlerta = LocalDateTime.now();
}
