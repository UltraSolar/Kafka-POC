package com.poc.kafka_order_poc.kafka;

import com.poc.kafka_order_poc.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = "order-events",
            groupId = "notification-service"
    )
    public void consume(OrderCreatedEvent event) {

        System.out.println(
                "Notification sent to " +
                event.getCustomerEmail() +
                " for order " +
                event.getOrderId()
        );
    }
}