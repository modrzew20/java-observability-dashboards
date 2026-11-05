package com.github.dm.notification.consumer;

import com.github.dm.notification.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private static final String TOPIC = "order-events";

    private final ConcurrentHashMap<String, NotificationEvent> notificationStore = new ConcurrentHashMap<>();

    @KafkaListener(topics = TOPIC, groupId = "notification-group")
    public void consumeOrderEvent(Map<String, Object> message) {
        log.info("Received order event from Kafka: {}", message);

        NotificationEvent notification = new NotificationEvent();
        notification.setOrderId((String) message.get("orderId"));
        notification.setCustomerName((String) message.get("customerName"));
        notification.setProduct((String) message.get("product"));
        notification.setAmount(((Number) message.get("amount")).doubleValue());
        notification.setStatus((String) message.get("status"));
        notification.setTimestamp(LocalDateTime.now());
        notification.setChannel("email");
        notification.setSent(true);

        notificationStore.put(notification.getOrderId(), notification);

        log.info("Notification processed for order: {} — channel: {}", notification.getOrderId(), notification.getChannel());
    }

    public ConcurrentHashMap<String, NotificationEvent> getNotifications() {
        return notificationStore;
    }
}
