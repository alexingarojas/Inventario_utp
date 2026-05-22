package com.tienda.inventario.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Representa la cantidad de un producto en un almacén específico
@Entity
@Table(name = "stocks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "almacen_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id", nullable = false)
    private Almacen almacen;

    @Column(nullable = false)
    private Integer cantidad;

    // Cantidad reservada que no está disponible
    @Column(name = "cantidad_reservada", nullable = false)
    @Builder.Default
    private Integer cantidadReservada = 0;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    @PreUpdate
    protected void antesDeGuardar() {
        ultimaActualizacion = LocalDateTime.now();
    }

    // Retorna cuánto hay disponible descontando reservas
    public Integer getCantidadDisponible() {
        return cantidad - cantidadReservada;
    }
}