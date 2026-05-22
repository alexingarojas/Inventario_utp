package com.tienda.inventario.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Representa una sede o almacén de la tienda
@Entity
@Table(name = "almacenes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único del almacén ej: ALM-LIMA, ALM-CAJA
    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    private String direccion;

    private String ciudad;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void antesDeGuardar() {
        fechaRegistro = LocalDateTime.now();
    }
}