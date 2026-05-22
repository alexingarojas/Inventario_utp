package com.tienda.inventario.patron.comando;

import com.tienda.inventario.modelo.Almacen;
import com.tienda.inventario.modelo.Movimiento;
import com.tienda.inventario.modelo.Producto;
import com.tienda.inventario.repositorio.MovimientoRepositorio;

// Comando concreto que guarda un movimiento en la base de datos
public class RegistrarMovimiento implements ComandoMovimiento {

    private final MovimientoRepositorio movimientoRepositorio;
    private final Producto producto;
    private final Almacen almacenOrigen;
    private final Almacen almacenDestino;
    private final Movimiento.TipoMovimiento tipo;
    private final Integer cantidad;
    private final String referencia;
    private final String notas;

    public RegistrarMovimiento(MovimientoRepositorio movimientoRepositorio,
                               Producto producto,
                               Almacen almacenOrigen,
                               Almacen almacenDestino,
                               Movimiento.TipoMovimiento tipo,
                               Integer cantidad,
                               String referencia,
                               String notas) {
        this.movimientoRepositorio = movimientoRepositorio;
        this.producto = producto;
        this.almacenOrigen = almacenOrigen;
        this.almacenDestino = almacenDestino;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.referencia = referencia;
        this.notas = notas;
    }

    @Override
    public void ejecutar() {
        Movimiento movimiento = Movimiento.builder()
                .producto(producto)
                .almacenOrigen(almacenOrigen)
                .almacenDestino(almacenDestino)
                .tipoMovimiento(tipo)
                .cantidad(cantidad)
                .referencia(referencia)
                .notas(notas)
                .build();

        movimientoRepositorio.save(movimiento);
        System.out.println("✓ Movimiento registrado: " + tipo
                + " - Producto: " + producto.getNombre()
                + " - Cantidad: " + cantidad);
    }
}