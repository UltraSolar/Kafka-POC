package com.poc.kafka_order_poc.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotBlank
    private String productId;

    @Min(1)
    private Integer quantity;

    @NotBlank
    @Email
    private String customerEmail;
}