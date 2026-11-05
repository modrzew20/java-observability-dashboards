package com.github.dm.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String orderId;
    private String customerName;
    private String product;
    private double amount;
    private String status;
    private LocalDateTime timestamp;

    public static OrderEvent create(String customerName, String product, double amount) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(UUID.randomUUID().toString());
        event.setCustomerName(customerName);
        event.setProduct(product);
        event.setAmount(amount);
        event.setStatus("CREATED");
        event.setTimestamp(LocalDateTime.now());
        return event;
    }
}
