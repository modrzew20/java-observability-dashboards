package com.github.dm.order.producer;

import com.github.dm.order.model.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderEvent(OrderEvent orderEvent) {
        log.info("Sending order event to Kafka: {}", orderEvent.getOrderId());
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, orderEvent.getOrderId(), orderEvent);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Order event sent successfully: {} to topic {}", orderEvent.getOrderId(), result.getRecordMetadata().topic());
            } else {
                log.error("Failed to send order event: {}", orderEvent.getOrderId(), ex);
            }
        });
    }
}
