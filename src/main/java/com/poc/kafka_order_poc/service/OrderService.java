package com.poc.kafka_order_poc.service;

import com.poc.kafka_order_poc.dto.CreateOrderRequest;
import com.poc.kafka_order_poc.dto.OrderResponse;
import com.poc.kafka_order_poc.modal.Order;
import com.poc.kafka_order_poc.repository.OrderRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();

        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderResponse response = new OrderResponse();

        response.setId(savedOrder.getId());
        response.setProductId(savedOrder.getProductId());
        response.setQuantity(savedOrder.getQuantity());
        response.setCustomerEmail(savedOrder.getCustomerEmail());
        response.setStatus(savedOrder.getStatus());
        response.setCreatedAt(savedOrder.getCreatedAt());

        return response;
    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

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