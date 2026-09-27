package com.poc.kafka_order_poc.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long id;
    private String productId;
    private Integer quantity;
    private String customerEmail;
    private String status;
    private LocalDateTime createdAt;
}