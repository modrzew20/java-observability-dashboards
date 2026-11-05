package com.github.dm.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String orderId;
    private String customerName;
    private String product;
    private double amount;
    private String status;
    private LocalDateTime timestamp;
    private String channel;
    private boolean sent;
}
