package com.poc.kafka_order_poc.mapper;

import com.poc.kafka_order_poc.dto.OrderResponse;
import com.poc.kafka_order_poc.model.Order;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setProductId(order.getProductId());
        response.setQuantity(order.getQuantity());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }
}