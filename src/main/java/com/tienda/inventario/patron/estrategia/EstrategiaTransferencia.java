package com.tienda.inventario.patron.estrategia;

import com.tienda.inventario.modelo.Stock;

// Patrón Strategy - define las reglas para permitir una transferencia
public interface EstrategiaTransferencia {

    // Retorna true si se puede hacer la transferencia
    boolean puedeTransferir(Stock stockOrigen, int cantidadSolicitada);

    String getNombre();
}