package com.github.dm.order.service;

import com.github.dm.order.model.OrderEvent;
import com.github.dm.order.producer.OrderEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderEventProducer orderEventProducer;
    private final WebClient notificationServiceWebClient;
    private final ConcurrentHashMap<String, OrderEvent> orderStore = new ConcurrentHashMap<>();

    public OrderService(OrderEventProducer orderEventProducer, WebClient notificationServiceWebClient) {
        this.orderEventProducer = orderEventProducer;
        this.notificationServiceWebClient = notificationServiceWebClient;
    }

    public OrderEvent createOrder(String customerName, String product, double amount) {
        OrderEvent order = OrderEvent.create(customerName, product, amount);

        // Send to Kafka (async)
        orderEventProducer.sendOrderEvent(order);

        // Call notification service via WebClient (sync HTTP)
        sendNotificationViaWebClient(order);

        orderStore.put(order.getOrderId(), order);
        log.info("Order created: {}", order.getOrderId());
        return order;
    }

    public List<OrderEvent> getAllOrders() {
        return new ArrayList<>(orderStore.values());
    }

    public OrderEvent getOrderById(String orderId) {
        return orderStore.get(orderId);
    }

    private void sendNotificationViaWebClient(OrderEvent order) {
        try {
            Mono<String> response = notificationServiceWebClient
                    .post()
                    .uri("/api/notifications/send")
                    .bodyValue(order)
                    .retrieve()
                    .bodyToMono(String.class);

            response.subscribe(
                    result -> log.info("Notification sent via WebClient: {}", result),
                    error -> log.error("Failed to send notification via WebClient", error)
            );
        } catch (WebClientResponseException e) {
            log.error("WebClient error when sending notification: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Exception when calling notification service", e);
        }
    }
}
