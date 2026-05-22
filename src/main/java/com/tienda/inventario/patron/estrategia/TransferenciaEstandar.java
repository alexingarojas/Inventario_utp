package com.tienda.inventario.patron.estrategia;

import com.tienda.inventario.modelo.Stock;
import org.springframework.stereotype.Component;

// Estrategia simple: transfiere si hay stock disponible suficiente
@Component("transferenciaEstandar")
public class TransferenciaEstandar implements EstrategiaTransferencia {

    @Override
    public boolean puedeTransferir(Stock stockOrigen, int cantidadSolicitada) {
        return stockOrigen.getCantidadDisponible() >= cantidadSolicitada;
    }

    @Override
    public String getNombre() {
        return "ESTANDAR";
    }
}