package com.tienda.inventario.patron.observador;

import com.tienda.inventario.dto.NotificacionDto;
import com.tienda.inventario.kafka.InventarioProducer;
import com.tienda.inventario.modelo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Observador que genera alertas cuando el stock baja del mínimo
@Component
public class AlertaStockBajo implements ObservadorStock {
    @Autowired
    InventarioProducer producer;

    @Override
    public void alCambiarStock(Stock stock, int cantidadAnterior) {
        int disponible = stock.getCantidadDisponible();
        int minimo = stock.getProducto().getStockMinimo();

        if (disponible <= minimo) {
            NotificacionDto mensaje = new NotificacionDto();
            mensaje.setProductoNombre(stock.getProducto().getNombre());
            mensaje.setAlmacenNombre(stock.getAlmacen().getNombre());
            mensaje.setDisponible(disponible);
            mensaje.setMinimo(minimo);
            System.out.println("⚠️ ALERTA: Stock bajo en producto '"
                    + stock.getProducto().getNombre()
                    + "' en almacén '"
                    + stock.getAlmacen().getNombre()
                    + "' - Disponible: " + disponible
                    + " / Mínimo: " + minimo);
            producer.enviarMensaje(mensaje);
        }
    }
}