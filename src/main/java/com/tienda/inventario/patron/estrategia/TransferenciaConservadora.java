package com.tienda.inventario.patron.estrategia;

import com.tienda.inventario.modelo.Stock;
import org.springframework.stereotype.Component;

// Estrategia conservadora: solo transfiere si queda stock mínimo después
@Component("transferenciaConservadora")
public class TransferenciaConservadora implements EstrategiaTransferencia {

    @Override
    public boolean puedeTransferir(Stock stockOrigen, int cantidadSolicitada) {
        int despuesDeTransferir = stockOrigen.getCantidadDisponible() - cantidadSolicitada;
        // No permite transferir si queda por debajo del stock mínimo
        return despuesDeTransferir >= stockOrigen.getProducto().getStockMinimo();
    }

    @Override
    public String getNombre() {
        return "CONSERVADORA";
    }
}