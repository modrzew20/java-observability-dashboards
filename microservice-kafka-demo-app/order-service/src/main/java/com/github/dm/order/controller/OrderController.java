package com.github.dm.order.controller;

import com.github.dm.order.model.OrderEvent;
import com.github.dm.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderEvent> createOrder(@RequestBody Map<String, Object> request) {
        String customerName = (String) request.get("customerName");
        String product = (String) request.get("product");
        double amount = Double.parseDouble(request.get("amount").toString());

        OrderEvent order = orderService.createOrder(customerName, product, amount);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderEvent>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEvent> getOrderById(@PathVariable String orderId) {
        OrderEvent order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
}
