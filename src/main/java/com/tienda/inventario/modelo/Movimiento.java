package com.tienda.inventario.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Registra cada movimiento del inventario (entrada, salida, transferencia, reserva)
@Entity
@Table(name = "movimientos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {

    public enum TipoMovimiento {
        ENTRADA,       // Se agrega stock
        SALIDA,        // Se reduce stock
        TRANSFERENCIA, // Se mueve entre almacenes
        RESERVA,       // Se reserva stock
        CANCELAR_RESERVA // Se cancela una reserva
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "almacen_origen_id")
    private Almacen almacenOrigen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "almacen_destino_id")
    private Almacen almacenDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = false)
    private Integer cantidad;

    // Referencia externa, ej: número de orden
    private String referencia;

    private String notas;

    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;

    @PrePersist
    protected void antesDeGuardar() {
        fechaMovimiento = LocalDateTime.now();
    }
}