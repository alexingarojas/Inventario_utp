package com.tienda.inventario.kafka;

import com.tienda.inventario.dto.NotificacionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventarioConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventarioConsumer.class);

    @KafkaListener(topics = "mi-topico-inventario", groupId = "inventario-group")
    public void consumirMensaje(NotificacionDto alerta) {
        LOGGER.info("¡ALERTA RECIBIDA! Producto: {} | Almacén: {} | Disponible: {}/{}",
                alerta.getProductoNombre(),
                alerta.getAlmacenNombre(),
                alerta.getDisponible(),
                alerta.getMinimo());
    }
}
