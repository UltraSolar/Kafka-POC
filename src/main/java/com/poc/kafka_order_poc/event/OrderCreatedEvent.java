package com.poc.kafka_order_poc.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private String productId;
    private Integer quantity;
    private String customerEmail;
    private LocalDateTime createdAt;
}