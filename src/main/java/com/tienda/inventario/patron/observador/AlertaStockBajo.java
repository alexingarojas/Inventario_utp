package com.tienda.inventario.patron.observador;

import com.tienda.inventario.modelo.Stock;
import org.springframework.stereotype.Component;

// Observador que genera alertas cuando el stock baja del mínimo
@Component
public class AlertaStockBajo implements ObservadorStock {

    @Override
    public void alCambiarStock(Stock stock, int cantidadAnterior) {
        int disponible = stock.getCantidadDisponible();
        int minimo = stock.getProducto().getStockMinimo();

        if (disponible <= minimo) {
            // TODO: aquí se podría enviar email o notificación push
            System.out.println("⚠️ ALERTA: Stock bajo en producto '"
                    + stock.getProducto().getNombre()
                    + "' en almacén '"
                    + stock.getAlmacen().getNombre()
                    + "' - Disponible: " + disponible
                    + " / Mínimo: " + minimo);
        }
    }
}