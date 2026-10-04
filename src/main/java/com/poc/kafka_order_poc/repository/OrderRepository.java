package com.poc.kafka_order_poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poc.kafka_order_poc.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
