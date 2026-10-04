package com.poc.kafka_order_poc.mapper;

import com.poc.kafka_order_poc.dto.OrderResponse;
import com.poc.kafka_order_poc.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);
}