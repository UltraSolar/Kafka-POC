package com.poc.kafka_order_poc.kafka;

import com.poc.kafka_order_poc.event.OrderCreatedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    private final RedisTemplate<String, Integer> redisTemplate;

    public InventoryConsumer(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void consume(OrderCreatedEvent event) {

        System.out.println(">>> InventoryConsumer received event: " + event);

        String key = "inventory:" + event.getProductId();

        Integer currentStock = redisTemplate.opsForValue().get(key);

        // If the product is not in Redis, we can assume an initial stock of 100 for demonstration purposes.
        if (currentStock == null) {
            currentStock = 100;
        }

        // Update the stock based on the order quantity
        int updatedStock = currentStock - event.getQuantity();

        // Update the stock in Redis
        redisTemplate.opsForValue().set(key, updatedStock);

        System.out.println(
                "Inventory updated: " +
                event.getProductId() +
                " -> " +
                updatedStock
        );
    }
    
}
