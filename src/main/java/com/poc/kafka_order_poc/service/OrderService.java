package com.poc.kafka_order_poc.service;

import com.poc.kafka_order_poc.dto.CreateOrderRequest;
import com.poc.kafka_order_poc.dto.OrderResponse;
import com.poc.kafka_order_poc.event.OrderCreatedEvent;
import com.poc.kafka_order_poc.exception.OrderNotFoundException;
import com.poc.kafka_order_poc.kafka.OrderEventProducer;
import com.poc.kafka_order_poc.mapper.OrderMapper;
import com.poc.kafka_order_poc.model.Order;
import com.poc.kafka_order_poc.repository.OrderRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(CreateOrderRequest request) {

        Order order = new Order();

        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getCustomerEmail(),
                savedOrder.getCreatedAt());

        orderEventProducer.publishOrderCreated(event);

        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}