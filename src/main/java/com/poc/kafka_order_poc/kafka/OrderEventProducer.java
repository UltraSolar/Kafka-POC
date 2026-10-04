package com.poc.kafka_order_poc.kafka;

import com.poc.kafka_order_poc.event.OrderCreatedEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(OrderCreatedEvent event) {

        try {
            kafkaTemplate
                    .send(TOPIC, event.getOrderId().toString(), event)
                    .get(2, TimeUnit.SECONDS);

            System.out.println(
                    "OrderCreated event published successfully");

        } catch (TimeoutException e) {

            throw new RuntimeException(
                    "Kafka unavailable: event publishing timed out", e);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to publish OrderCreated event", e);
        }
    }
}