package com.tienda.inventario.patron.observador;

import com.tienda.inventario.modelo.Stock;

// Patrón Observer - interfaz para recibir alertas de cambios en stock
public interface ObservadorStock {
    void alCambiarStock(Stock stock, int cantidadAnterior);
}