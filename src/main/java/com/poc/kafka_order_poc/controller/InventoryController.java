package com.poc.kafka_order_poc.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final RedisTemplate<String, Integer> redisTemplate;

    public InventoryController(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{productId}")
    public Integer getInventory(@PathVariable String productId) {

        String key = "inventory:" + productId;

        Integer stock = redisTemplate.opsForValue().get(key);

        if (stock == null) {
            return 0;
        }

        return stock;
    }
}