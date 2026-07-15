package com.tienda.inventario.kafka;

import com.tienda.inventario.dto.NotificacionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class InventarioProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventarioProducer.class);
    private final KafkaTemplate<String, NotificacionDto> kafkaTemplate;

    public InventarioProducer(KafkaTemplate<String, NotificacionDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensaje(NotificacionDto mensaje) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(5000); // 2 segundos de espera
                LOGGER.info("Enviando mensaje retrasado a Kafka: {}", mensaje);
                kafkaTemplate.send("mi-topico-inventario", mensaje);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
