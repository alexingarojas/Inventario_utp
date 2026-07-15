package com.tienda.inventario.kafka;

import com.tienda.inventario.dto.NotificacionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventarioConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventarioConsumer.class);

    private final SimpMessagingTemplate messagingTemplate;

    public InventarioConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "mi-topico-inventario", groupId = "inventario-group")
    public void consumirAlerta(NotificacionDto alerta) {
        LOGGER.info("Consumiendo alerta de Kafka: {}", alerta.getProductoNombre());
        messagingTemplate.convertAndSend("/topic/alertas", alerta);
    }
}
