package com.github.dm.notification.controller;

import com.github.dm.notification.consumer.OrderEventConsumer;
import com.github.dm.notification.model.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final OrderEventConsumer orderEventConsumer;

    public NotificationController(OrderEventConsumer orderEventConsumer) {
        this.orderEventConsumer = orderEventConsumer;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> orderData) {
        log.info("Received notification request via WebClient: {}", orderData);
        return ResponseEntity.ok("Notification received for order: " + orderData.get("orderId"));
    }

    @GetMapping
    public ResponseEntity<List<NotificationEvent>> getAllNotifications() {
        List<NotificationEvent> notifications = new ArrayList<>(orderEventConsumer.getNotifications().values());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getNotificationCount() {
        int count = orderEventConsumer.getNotifications().size();
        return ResponseEntity.ok(Map.of("count", count, "message", "Total notifications processed"));
    }
}
